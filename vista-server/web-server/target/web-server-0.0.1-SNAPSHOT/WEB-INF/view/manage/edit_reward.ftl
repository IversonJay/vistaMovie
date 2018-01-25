<div style="margin:20px 5px">
    <form name="theform" method="post" id="theform">
        <table style="font-size: 12px;border-spacing:20px 0px">
            <tr height="20px">
                <td>奖品ID：</td>
            </tr>
            <tr height="25px">
                <td><input id="rid" name="rid" type="text" class="easyui-textbox"
                           data-options="required:true,missingMessage:'奖品ID不能为空'"
                           style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">类型：</td>
            </tr>
            <tr height="25px">
                <td>
                    <select id="rtype" name="rtype" class="easyui-combobox"
                            data-options="editable:false,panelHeight:46,width:292,height:25">
                        <option value="1" selected="selected">大转盘</option>
                    </select>
                </td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">中奖概率：</td>
            </tr>
            <tr height="20px">
                <td><input id="rchance" name="rchance" type="text" class="easyui-numberbox"
                           data-options="required:true,missingMessage:'中奖概率不能为空',min:0"
                           style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">备注：</td>
            </tr>
            <tr height="25px">
                <td><input id="remark" name="remark" type="text" class="easyui-textbox"
                           data-options="validType:'length[0,50]',invalidMessage:'备注需在0到50位之间'"
                           style="width:292px;height:25px"/></td>
            </tr>
        </table>
    </form>
</div>
