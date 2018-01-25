<div style="margin:20px 5px">
    <form name="theform" method="post" id="theform">
    <input name="key" id="key" type="hidden"/>
        <table style="font-size: 12px;border-spacing:20px 0px">
            <tr height="20px">
                <td>消息内容</td>
            </tr>
            <tr height="25px">
                <td><input id="value" name="value" type="text" class="easyui-textbox" 
                           data-options="required:true,missingMessage:'消息内容不能为空'"
                           style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">消息类型：</td>
            </tr>
            <tr height="25px">
                <td>
                    <select id="mtype" name="mtype" class="easyui-combobox"
                            data-options="editable:false,panelHeight:70,width:292,height:25">
                        <option value="0">外置链接</option>
                        <option value="1">电影链接</option>
                        <option value="2">进入首页</option>
                    </select>
                </td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">链接:</td>
            </tr>
            <tr height="25px">
                <td><input id="target" name="target" type="text" class="easyui-textbox"
                           data-options="required:true,missingMessage:'如为上方选择<b>电影链接</b>务必填写合并影片中的影片标识'"
                           style="width:292px;height:25px"/></td>
            </tr>
        </table>
    </form>
</div>
