<div style="margin:20px 5px">
    <form name="theform" method="post" id="theform">
        <input name="id" id="id" type="hidden"/>
        <table style="font-size: 12px;border-spacing:20px 0px">
            <tr height="20px">
                <td>用户名：</td>
            </tr>
            <tr height="25px">
                <td><input id="username" name="username" type="text" class="easyui-textbox"
                           data-options="required:true,missingMessage:'用户名不能为空',validType:'length[2,15]',invalidMessage:'用户名需在2到15位之间'"
                           style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">密码：</td>
            </tr>
            <tr height="20px">
                <td><input id="password" name="password" type="password" class="easyui-textbox"
                           data-options="required:true,missingMessage:'密码不能为空',validType:'length[6,15]',invalidMessage:'密码需在6到15位之间'"
                           style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">用户角色：</td>
            </tr>
            <tr height="25px">
                <td>
                    <select id="role" name="role" class="easyui-combobox"
                            data-options="editable:false,panelHeight:46,width:292,height:25">
                        <option value="2" selected="selected">普通用户</option>
                        <option value="1">系统管理员</option>
                    </select>
                </td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">是否锁定：</td>
            </tr>
            <tr height="25px">
                <td>
                    <select id="state" name="state" class="easyui-combobox"
                            data-options="editable:false,panelHeight:46,width:292,height:25">
                        <option value="-1" selected="selected">锁定</option>
                        <option value="1">未锁定</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
