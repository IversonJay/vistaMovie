<div style="margin:20px 5px">
    <form name="theform" method="post" id="theform">
        <input name="id" id="id" type="hidden"/>
        <table style="font-size: 12px;border-spacing:20px 0px">
            <tr height="20px">
                <td>活动名称：</td>
                <td style="padding-top:20px">活动状态：</td>
            </tr>
            <tr height="25px">
                <td><input id="aname" name="aname" type="text" class="easyui-textbox"
                           data-options="required:true,missingMessage:'活动名称不能为空'"
                           style="width:292px;height:25px"/></td>
                <td><select id="state" name="state" class="easyui-combobox"
                            data-options="editable:false,panelHeight:46,width:292,height:25">
                    <option value="1" selected="selected">开启</option>
                    <option value="-1">关闭</option>
                </select></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px" colspan="2">活动封面图：</td>
            </tr>
            <tr height="25px">
                <td colspan="2"><input id="coverFileTemp" name="coverFileTemp" type="text" class="easyui-file"
                                       data-options="required:true,missingMessage:'活动封面图不能为空'"
                                       style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">开始时间：</td>
                <td style="padding-top:20px">结束时间：</td>
            </tr>
            <tr height="25px">
                <td><input id="sdatetime" name="sdatetime" type="text" class="easyui-datetimebox"
                           data-options="required:true,missingMessage:'开始时间不能为空'"
                           style="width:292px;height:25px"/></td>
                <td><input id="edatetime" name="edatetime" type="text" class="easyui-datetimebox"
                           data-options="required:true,missingMessage:'结束时间不能为空'"
                           style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px" colspan="2">活动详情：</td>
            </tr>
            <tr>
                <td colspan="2"><textarea id="content" name="content" style="width: 606px;height: 400px;"></textarea></td>
            </tr>
        </table>
    </form>
</div>
