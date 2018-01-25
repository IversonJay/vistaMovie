<div style="margin:20px 5px">
    <form name="theform" method="post" id="theform">
        <input name="id" id="id" type="hidden"/>
        <table style="font-size: 12px;border-spacing:20px 0px">
            <tr height="20px">
                <td>城市名：</td>
            </tr>
            <tr height="25px">
                <td><input id="cityName" name="cityName" type="text" class="easyui-textbox"
                           data-options="required:true,missingMessage:'城市名不能为空'"
                           style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">城市拼音(用于和vista系统对接)：</td>
            </tr>
            <tr height="20px">
                <td><input id="ename" name="ename" type="text" class="easyui-textbox"
                           data-options="required:true,missingMessage:'城市拼音不能为空'"
                           style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">拼音首字母：</td>
            </tr>
            <tr height="20px">
                <td><input id="pinyin" name="pinyin" type="text" class="easyui-textbox"
                           data-options="required:true,missingMessage:'拼音首字母不能为空'"
                           style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">是否是热点城市：</td>
            </tr>
            <tr height="25px">
                <td>
                    <select id="hot" name="hot" class="easyui-combobox"
                            data-options="editable:false,panelHeight:46,width:292,height:25">
                        <option value="1" selected="selected">是</option>
                        <option value="2">否</option>
                    </select>
                </td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">经度：</td>
            </tr>
            <tr height="20px">
                <td><input id="lon" name="lon" type="text" class="easyui-numberbox"
                           data-options="required:true,missingMessage:'经度不能为空',min:0,precision:8"
                           style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">纬度：</td>
            </tr>
            <tr height="25px">
                <td><input id="lat" name="lat" type="text" class="easyui-numberbox"
                           data-options="required:true,missingMessage:'纬度不能为空',min:0,precision:8"
                           style="width:292px;height:25px"/></td>
            </tr>
        </table>
    </form>
</div>
