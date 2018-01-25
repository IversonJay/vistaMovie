<#import "/spring.ftl" as spring />

<#macro appname>${springMacroRequestContext.getContextPath()}/</#macro>

<#macro url u><@pcom.appname/>${u}</#macro>

<#macro required><span class="required">*</span></#macro>

<#macro page>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
    <meta charset="utf-8"/>
    <title></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <meta name="MobileOptimized" content="320">
</head>
    <#nested>
</html>
</#macro>

<#macro manage>

<script type="text/javascript" src="<@pcom.appname/>common/jslibs/jquery.min.js"></script>
<script type="text/javascript" src="<@pcom.appname/>common/jslibs/jquery.cookie.min.js"></script>
<script type="text/javascript" src="<@pcom.appname/>common/jslibs/jquery.extends.js"></script>

<script type="text/javascript" src="<@pcom.appname/>common/jslibs/json2.js"></script>
<script type="text/javascript" src="<@pcom.appname/>common/jslibs/util.js"></script>

<script type="text/javascript" src="<@pcom.appname/>common/jslibs/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<@pcom.appname/>common/jslibs/easyui/easyui-lang-zh_CN.js"></script>

<link href="<@pcom.appname/>common/jslibs/easyui/themes/icon.css" rel="stylesheet" type="text/css"/>
<link href="<@pcom.appname/>common/jslibs/easyui/themes/micon.css" rel="stylesheet" type="text/css"/>
    <@pcom.page>
        <#nested>
    </@pcom.page>
</#macro>