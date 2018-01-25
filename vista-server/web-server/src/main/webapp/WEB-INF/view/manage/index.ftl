<@pcom.manage>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.base.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.main.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.manager.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.city.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.roll.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.ads.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.activity.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.rewardControl.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.systemValue.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.movie.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.movieR.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.ticketZX.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.ticketHS.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.connectionXS.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.connectionCZ.js'/>"></script>
<script type="text/javascript" src="<@pcom.url 'common/js/manage/page.statistics.js'/>"></script>

<link rel="stylesheet" href="<@pcom.appname/>common/jslibs/plugins/kindeditor/themes/default/default.css" />
<script charset="utf-8" src="<@pcom.appname/>common/jslibs/plugins/kindeditor/kindeditor.js"></script>
<script charset="utf-8" src="<@pcom.appname/>common/jslibs/plugins/kindeditor/lang/zh_CN.js"></script>
<style>
    .tree .tree-node {
        height: 26px;
    }

    .tree .tree-node span {
        margin-top: 4px;
    }
</style>
</head>
<body id="mainLayout">

</body>
<script type="text/javascript">
    $.fn.baseOptions.appUrl = "<@pcom.appname/>";
    $.fn.baseOptions.easyuiroot = "common/jslibs/easyui";
    $(function () {
        $("#mainLayout").page_main({username: "${username?default('')}"});
    });
</script>
</@pcom.manage>