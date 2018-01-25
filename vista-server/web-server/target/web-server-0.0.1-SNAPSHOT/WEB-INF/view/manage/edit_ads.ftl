<div style="margin:20px 5px">
    <form name="theform" method="post" id="theform" enctype="multipart/form-data">
        <input name="id" id="id" type="hidden"/>
        <table style="font-size: 12px;border-spacing:20px 0px">
            <tr height="20px">
                <td>标题：</td>
            </tr>
            <tr height="25px">
                <td><input id="title" name="title" type="text" class="easyui-textbox"
                           data-options="required:true,missingMessage:'标题不能为空'"
                           style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">展示图片</td>
            </tr>
            <tr height="20px">
                <td colspan="2"><input id="coverFileTemp" name="coverFileTemp" type="text" class="easyui-filebox"
                                       data-options="required:true,missingMessage:'展示图片不能为空',prompt:'请选择一张图片文件',buttonText:'选择文件'"
                                       style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">是否启用：</td>
            </tr>
            <tr height="25px">
                <td>
                    <select id="isuse" name="isuse" class="easyui-combobox"
                            data-options="editable:false,panelHeight:55,width:292,height:25">
                        <option value="0">禁用</option>
                        <option value="1">启用</option>
                    </select>
                </td>
            </tr>
            
            <tr height="20px">
                <td style="padding-top:20px">广告类型：</td>
            </tr>
            <tr height="25px">
                <td>
                    <select id="type" name="type" class="easyui-combobox"
                            data-options="editable:false,panelHeight:55,width:292,height:25">
                        <option value="0">外置链接</option>
                        <option value="1">电影链接</option>
                    </select>
                </td>
            </tr>
            
            <tr height="20px">
                <td style="padding-top:20px">链接（如为上方选择<b>电影链接</b>务必填写合并影片中的影片标识）</td>
            </tr>
            <tr height="20px">
                <td><input id="targetUrl" name="targetUrl" type="text" class="easyui-textbox"
                           style="width:292px;height:25px"/></td>
            </tr>
            
        </table>
    </form>
</div>
