INSERT INTO synship.tm_code (id, code, name, name1, des, active, created, creater, modified, modifier) VALUES
('IS_MID_FTP', 'Domain', '10.0.0.44', '', 'Image Server 的 Ftp 设置', 1, now(), 'system', now(), 'system'),
('IS_MID_FTP', 'Port', '21', '', 'Image Server 的 Ftp 设置', 1, now(), 'system', now(), 'system'),
('IS_MID_FTP', 'UserName', 'root', '', 'Image Server 的 Ftp 设置', 1, now(), 'system', now(), 'system'),
('IS_MID_FTP', 'Password', '123456', '', 'Image Server 的 Ftp 设置', 1, now(), 'system', now(), 'system'),
('IS_MID_FTP', 'Encoding', 'utf-8', '', 'Image Server 的 Ftp 设置', 1, now(), 'system', now(), 'system');

SELECT * FROM synship.tm_code WHERE id = 'IS_MID_FTP';