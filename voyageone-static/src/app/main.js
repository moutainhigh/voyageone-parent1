require.config({
    baseUrl: '../',
    paths: {
        'vo-libs-angular': 'components/components.ng',
        'vo-libs': 'components/components',
        'angular': 'assets/js/angular.js/1.5.6/angular',
        'jquery': 'assets/js/jquery/2.2.4/jquery',
        'angularAMD': 'assets/js/angularAMD/0.2.1/angularAMD.min',
        'underscore': 'assets/js/underscore.js/1.8.3/underscore',
        'css': 'assets/js/require-css/0.1.8/css',
        'filestyle': 'assets/js/bootstrap-filestyle/1.2.1/bootstrap-filestyle',
        'notify': 'assets/js/notify/0.4.0/notify',
        'angular-block-ui': 'assets/js/angular-block-ui/0.2.1/angular-block-ui',
        'angular-cookies': 'assets/js/angular.js/1.5.6/angular-cookies',
        'angular-sanitize': 'assets/js/angular.js/1.5.6/angular-sanitize',
        'angular-route': 'assets/js/angular.js/1.5.6/angular-route',
        'angular-translate': 'assets/js/angular-translate/2.8.1/angular-translate',
        'angular-ui-bootstrap': 'assets/js/angular-ui-bootstrap/1.3.3/ui-bootstrap-tpls.min',
        'chosen': 'assets/js/chosen/1.4.2/chosen.jquery',
        'angular-chosen': 'assets/js/angular-chosen/1.2.0/angular-chosen',
        'angular-animate': 'assets/js/angular.js/1.5.6/angular-animate',
        'angular-ngStorage': 'assets/js/angular-ngStorage/ngStorage',
        'angular-file-upload': 'assets/js/angular-file-upload/2.2.0/angular-file-upload'
    },
    shim: {
        'vo-libs': ['jquery'],
        'vo-libs-angular': ['angular'],
        'angular-sanitize': ['angular'],
        'angular-route': ['angular'],
        'angular-animate': ['angular'],
        'angular-cookies': ['angular'],
        'angular-translate': ['angular'],
        'angular-block-ui': ['angular', 'css!assets/js/angular-block-ui/0.2.1/angular-block-ui.min.css'],
        'angular-ui-bootstrap': ['angular'],
        'angular-ngStorage': ['angular'],
        'angular-file-upload': ['angular'],
        'angular': {exports: 'angular', deps: ['jquery']},
        'jquery': {exports: 'jQuery'},
        'filestyle': ['jquery'],
        'angularAMD': ['angular'],
        'chosen': ['jquery', 'css!assets/js/chosen/1.4.2/chosen.min.css'],
        'angular-chosen': ['angular', 'chosen']
    },
    deps: ['app/app']
});

define('vo-vms', [
    'angularAMD',
    'underscore',
    'angular-sanitize',
    'angular-route',
    'angular-animate',
    'angular-cookies',
    'angular-translate',
    'angular-block-ui',
    'angular-ui-bootstrap',
    'vo-libs-angular'
], function (angularAMD, _) {

    var app = angular.module('vo.vms', [
        'ngSanitize',
        'ngRoute',
        'ngAnimate',
        'ngCookies',
        'pascalprecht.translate',
        'blockUI',
        'ui.bootstrap',
        'vo.ng'
    ]);

    return angularAMD.bootstrap(app);
});