package com.voyageone.task2.vms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.vms.VmsBtFinancialReportDao;
import com.voyageone.service.daoext.vms.VmsBtOrderDetailDaoExt;
import com.voyageone.service.model.vms.VmsBtFinancialReportModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.vms.VmsConstants;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 生成财务报表
 * Created on 16/06/29.
 * @author jeff.duan
 * @version 1.0
 */
@Service
public class VmsMakeFinancialReportService extends BaseCronTaskService {

    @Autowired
    private VmsBtFinancialReportDao vmsBtFinancialReportDao;

    @Autowired
    private VmsBtOrderDetailDaoExt vmsBtOrderDetailDaoExt;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.VMS;
    }

    @Override
    public String getTaskName() {
        return "VmsMakeFinancialReportJob";
    }


    /**
     * 生成财务报表
     *
     * @param taskControlList job 配置
     * @throws Exception
     */
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        if (orderChannelIdList != null && orderChannelIdList.size() > 0) {

            // 按渠道进行处理
            for (String orderChannelID : orderChannelIdList) {
                makeFinancialReport(orderChannelID);
            }
        }
    }

    /**
     * 生成财务报表
     *
     * @param channelId 渠道
     */
    public void makeFinancialReport(String channelId) throws ParseException, IOException {

        // 取得系统日期 - 1个月
        String date = DateTimeUtil.format(DateTimeUtil.addMonths(DateTimeUtil.getDate(), -1), DateTimeUtil.DATE_TIME_FORMAT_3);

        // 取得这个渠道出财务报表的日
//        String makeReportDay = "01";
//        VmsChannelConfigBean vmsChannelConfigBean = VmsChannelConfigs.getConfigBean(channelId
//                , VmsConstants.ChannelConfig.MAKE_FINANCIAL_REPORT_DAY
//                ,VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
//        if (vmsChannelConfigBean != null) {
//            String day = vmsChannelConfigBean.getConfigValue1();
//            if (!StringUtils.isEmpty(day) && day.length() <= 2) {
//                if (day.length() == 1) {
//                    day = "0" + day;
//                }
//                makeReportDay = day;
//            }
//        }

        // 看看vms_bt_financial_report表里有没有出过这个月的财务报表
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        param.put("reportYearMonth", date.substring(0,6));
        List<VmsBtFinancialReportModel> models = vmsBtFinancialReportDao.selectList(param);
        if (models.size() > 0) {
            return;
        }

        $info("财务报表生成开始，channelId" + channelId + ",年月：" + date.substring(0,6));

        // 取得这期财务报表的数据
        Map<String, Object> param1 = new HashMap<>();
        param1.put("channelId", channelId);
        param1.put("status", VmsConstants.STATUS_VALUE.PRODUCT_STATUS.RECEIVED);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strReceivedTimeFrom = date.substring(0,4) + "-" + date.substring(4,6) + "-01 00:00:00";
        Date receivedTimeFrom = sdf.parse(strReceivedTimeFrom);
        param1.put("receivedTimeFrom", receivedTimeFrom);
        param1.put("receivedTimeTo", DateTimeUtil.addMonths(receivedTimeFrom, 1));
        List<Map<String, Object>> reportDataList = vmsBtOrderDetailDaoExt.selectListByTime(param1);

        // 生成财务报表Excel
        makeFinancialReportExcel(channelId, strReceivedTimeFrom, reportDataList);

        $info("财务报表生成完了，channelId" + channelId + ",年月：" + date.substring(0,6));
    }

    /**
     * 生成财务报表Excel
     *
     * @param channelId 渠道
     * @param strReceivedTimeFrom 报表开始时间
     * @param reportDataList 报表数据
     *
     */
    public void makeFinancialReportExcel(String channelId, String strReceivedTimeFrom, List<Map<String, Object>> reportDataList) throws ParseException, IOException {

        OrderChannelBean channel = Channels.getChannel(channelId);

        String reportDateFrom = strReceivedTimeFrom.substring(0,4) + strReceivedTimeFrom.substring(5,7) + strReceivedTimeFrom.substring(8,10);

        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook();
        sxssfWorkbook.setCompressTempFiles(true);

        Font boldFont = sxssfWorkbook.createFont();
        boldFont.setBold(true);

        Font titleFont = sxssfWorkbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);


        Sheet sheet = sxssfWorkbook.createSheet(reportDateFrom.substring(0, 6));
        // 设置列宽
        // ShipmentName
        sheet.setColumnWidth(0, 7000);
        // OrderID
        sheet.setColumnWidth(1, 5000);
        // ReceivedTime
        sheet.setColumnWidth(2, 5500);
        // TrackingNo
        sheet.setColumnWidth(3, 5000);
        // Sku
        sheet.setColumnWidth(4, 6000);
        // Name
        sheet.setColumnWidth(5, 12000);
        // VoyagoOnePrice
        sheet.setColumnWidth(6, 4000);

        // 设置Header行格式
        CellStyle headerCellStyle1 = sxssfWorkbook.createCellStyle();
        headerCellStyle1.setFont(titleFont);

        CellStyle headerCellStyle2 = sxssfWorkbook.createCellStyle();
        headerCellStyle2.setFont(boldFont);


        // 设置单元格默认格式
        CellStyle defaultRowCellStyle = sxssfWorkbook.createCellStyle();
        defaultRowCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        defaultRowCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        defaultRowCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        defaultRowCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        defaultRowCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        defaultRowCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);


        // 标题行格式
        CellStyle titleRowCellStyle = sxssfWorkbook.createCellStyle();
        titleRowCellStyle.setFont(boldFont);
        titleRowCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleRowCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleRowCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleRowCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleRowCellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        titleRowCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleRowCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleRowCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        // 统计合计
        // 统计Order数量
        Set<String> orderSet = new HashSet();
        // 统计sku总价
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (int i = 0; i < reportDataList.size(); i++) {
            // OrderID
            String orderId = StringUtils.null2Space((String) reportDataList.get(i).get("consolidation_order_id"));
            if (!StringUtils.isEmpty(orderId)) {
                orderSet.add(orderId);
            }
            // VoyageOnePrice
            BigDecimal price = (BigDecimal) reportDataList.get(i).get("client_promotion_price");
            if (price != null) {
                totalPrice = totalPrice.add(price);
            }
        }

        /* 设置Header行 */
        Row headerRow1 = sheet.createRow(0);
        Cell headerTitleCell = headerRow1.createCell(0, Cell.CELL_TYPE_STRING);
        headerTitleCell.setCellValue("Financial Report");
        headerTitleCell.setCellStyle(headerCellStyle1);

        Row headerRow2 = sheet.createRow(1);
        // Company
        Cell headerCompanyCell = headerRow2.createCell(0, Cell.CELL_TYPE_STRING);
        headerCompanyCell.setCellValue("Company: " + channel.getFull_name());
        headerCompanyCell.setCellStyle(headerCellStyle2);

        //  Report Period
        Cell headerReportPeriodCell = headerRow2.createCell(1, Cell.CELL_TYPE_STRING);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date reportDateFromDate = sdf.parse(reportDateFrom);
        String reportDateTo = DateTimeUtil.format(DateTimeUtil.getMonthLastDay(reportDateFromDate), "yyyyMMdd");
        headerReportPeriodCell.setCellValue("Report Period: " + reportDateFrom + " - " + reportDateTo);
        headerReportPeriodCell.setCellStyle(headerCellStyle2);

        // OrderTotal
        Cell headerOrderTotalCell = headerRow2.createCell(3, Cell.CELL_TYPE_STRING);
        headerOrderTotalCell.setCellValue("OrderTotal: " + orderSet.size());
        headerOrderTotalCell.setCellStyle(headerCellStyle2);

        // SkuTotal
        Cell headerSkuTotalCell = headerRow2.createCell(4, Cell.CELL_TYPE_STRING);
        headerSkuTotalCell.setCellValue("SkuTotal: " + reportDataList.size());
        headerSkuTotalCell.setCellStyle(headerCellStyle2);

        // PriceTotal
        Cell headerPriceTotalCell = headerRow2.createCell(5, Cell.CELL_TYPE_STRING);
        headerPriceTotalCell.setCellValue("PriceTotal: $" + totalPrice.setScale(2,   BigDecimal.ROUND_HALF_UP).toString());
        headerPriceTotalCell.setCellStyle(headerCellStyle2);

        /* 设置标题行 */
        Row titleRow = sheet.createRow(3);
        // ShipmentName
        Cell titleShipmentNameCell = titleRow.createCell(0, Cell.CELL_TYPE_STRING);
        titleShipmentNameCell.setCellValue("ShipmentName");
        titleShipmentNameCell.setCellStyle(titleRowCellStyle);
        // OrderID
        Cell titleOrderIdCell = titleRow.createCell(1, Cell.CELL_TYPE_STRING);
        titleOrderIdCell.setCellValue("OrderID");
        titleOrderIdCell.setCellStyle(titleRowCellStyle);
        // ReceivedTime
        Cell titleReceivedTimeCell = titleRow.createCell(2, Cell.CELL_TYPE_STRING);
        titleReceivedTimeCell.setCellValue("ReceivedTime");
        titleReceivedTimeCell.setCellStyle(titleRowCellStyle);
        // TrackingNo
        Cell titleTrackingNoCell = titleRow.createCell(3, Cell.CELL_TYPE_STRING);
        titleTrackingNoCell.setCellValue("TrackingNo");
        titleTrackingNoCell.setCellStyle(titleRowCellStyle);
        // Sku
        Cell titleSkuCell = titleRow.createCell(4, Cell.CELL_TYPE_STRING);
        titleSkuCell.setCellValue("Sku");
        titleSkuCell.setCellStyle(titleRowCellStyle);
        // Name
        Cell titleNameCell = titleRow.createCell(5, Cell.CELL_TYPE_STRING);
        titleNameCell.setCellValue("Name");
        titleNameCell.setCellStyle(titleRowCellStyle);
        // VoyagoOnePrice
        Cell titleVoyagoOnePriceCell = titleRow.createCell(6, Cell.CELL_TYPE_STRING);
        titleVoyagoOnePriceCell.setCellValue("VoyageOnePrice");
        titleVoyagoOnePriceCell.setCellStyle(titleRowCellStyle);

        /* 设置内容行 */
        for (int i = 0; i < reportDataList.size(); i++) {

            Row contentRow = sheet.createRow(i + 4);

            // ShipmentName
            Cell contentShipmentNameCell = contentRow.createCell(0, Cell.CELL_TYPE_STRING);
            contentShipmentNameCell.setCellValue(StringUtils.null2Space((String) reportDataList.get(i).get("shipment_name")));
            contentShipmentNameCell.setCellStyle(defaultRowCellStyle);
            // OrderID
            String orderId = StringUtils.null2Space((String) reportDataList.get(i).get("consolidation_order_id"));
            Cell contentOrderIdCell = contentRow.createCell(1, Cell.CELL_TYPE_STRING);
            contentOrderIdCell.setCellValue(orderId);
            contentOrderIdCell.setCellStyle(defaultRowCellStyle);
            // ReceivedTime
            Cell contentReceivedTimeCell = contentRow.createCell(2, Cell.CELL_TYPE_STRING);
            Date receivedTime = (Date) reportDataList.get(i).get("received_time");
            if (receivedTime == null) {
                contentReceivedTimeCell.setCellValue("");
            } else {
                contentReceivedTimeCell.setCellValue(DateTimeUtil.format(receivedTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT));
            }
            contentReceivedTimeCell.setCellStyle(defaultRowCellStyle);
            // TrackingNo
            Cell contentTrackingNoCell = contentRow.createCell(3, Cell.CELL_TYPE_STRING);
            contentTrackingNoCell.setCellValue(StringUtils.null2Space((String) reportDataList.get(i).get("tracking_no")));
            contentTrackingNoCell.setCellStyle(defaultRowCellStyle);
            // Sku
            Cell contentSkuCell = contentRow.createCell(4, Cell.CELL_TYPE_STRING);
            contentSkuCell.setCellValue(StringUtils.null2Space((String) reportDataList.get(i).get("client_sku")));
            contentSkuCell.setCellStyle(defaultRowCellStyle);
            // Name
            Cell contentNameCell = contentRow.createCell(5, Cell.CELL_TYPE_STRING);
            contentNameCell.setCellValue(StringUtils.null2Space((String) reportDataList.get(i).get("name")));
            contentNameCell.setCellStyle(defaultRowCellStyle);
            // VoyageOnePrice
            Cell contentVoyageOnePriceCell = contentRow.createCell(6, Cell.CELL_TYPE_STRING);
            BigDecimal price = (BigDecimal) reportDataList.get(i).get("client_promotion_price");
            if (price == null) {
                contentVoyageOnePriceCell.setCellValue("");
            } else {
                contentVoyageOnePriceCell.setCellValue("$" + price.toString());
            }
            contentVoyageOnePriceCell.setCellStyle(defaultRowCellStyle);
        }



        //新建文件输出流
        // 生成财务报表路径
        String reportFilePath = Codes.getCodeName(VmsConstants.VMS_PROPERTY, "vms.report");
        reportFilePath += "/" + channelId + "/";
        // 创建文件目录
        FileUtils.mkdirPath(reportFilePath);
        // 财务报表文件名
        String feedErrorFileName = "Financial_Report_" + channelId + "_" + reportDateFrom.substring(0, 6) + ".xlsx";
        FileOutputStream out = new FileOutputStream(reportFilePath + feedErrorFileName);

        sxssfWorkbook.write(out);
        out.flush();
        out.close();


        // 更新文件管理信息
        VmsBtFinancialReportModel model = new VmsBtFinancialReportModel();
        model.setChannelId(channelId);
        model.setReportYearMonth(reportDateFrom.substring(0, 6));
        model.setReportStartDate(reportDateFrom);
        model.setReportEndDate(reportDateTo);
        model.setReportFileName(feedErrorFileName);
        model.setTotalPrice(totalPrice.setScale(2,   BigDecimal.ROUND_HALF_UP).toString());
        model.setStatus(VmsConstants.FinancialReportStatus.UNCONFIRMED);
        model.setModifier(getTaskName());
        vmsBtFinancialReportDao.insert(model);
    }
}