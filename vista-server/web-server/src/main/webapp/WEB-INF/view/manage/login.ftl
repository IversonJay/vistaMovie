<@pcom.manage>
<link href="<@pcom.appname/>common/jslibs/easyui/themes/default/easyui.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
    function submit() {
        $('#theForm').submit();
    }
</script>
<div class="easyui-window" title="登录" style="width:400px;padding:30px 60px"
     data-options="resizable:false,draggable:false,collapsible:false,minimizable:false,maximizable:false,closable:false">
    <form id="theForm" action="<@pcom.appname/>m_common/login" method="post">
        <div style="margin-bottom:20px">
            <div>帐号:</div>
            <input class="easyui-textbox" type="text" name="username" id="username" value=""
                   data-options="required:true,missingMessage:'用户名必须填写'" style="width:100%;height:32px">
        </div>
        <div style="margin-bottom:20px">
            <div>密码:</div>
            <input class="easyui-textbox" type="password" name="password" id="password"  value=""
                   data-options="required:true,missingMessage:'密码必须填写'" style="width:100%;height:32px">
        </div>
        <div style="margin-bottom:20px">
            <a href="javascript:submit();" class="easyui-linkbutton" iconCls="icon-ok"
               style="width:100%;height:32px">登陆</a>
        </div>
        <div>
            <center style="font-size: 12px;">&copy;&copy;&nbsp;&nbsp;版权所有</center>
        </div>
    </form>
</div>
</@pcom.manage>