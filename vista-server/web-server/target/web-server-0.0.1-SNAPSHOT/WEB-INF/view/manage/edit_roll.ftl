<div style="margin:20px 5px">
    <form name="theform" method="post" id="theform" enctype="multipart/form-data">
        <input name="id" id="id" type="hidden"/>
        <table style="font-size: 12px;border-spacing:20px 0px">
            <tr height="20px">
                <td>标题：</td>
            </tr>
            <tr height="25px">
                <td><input id="title" name="title" type="text" class="easyui-textbox"
                           data-options="required:true,missingMessage:'城市名不能为空'"
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
                <td style="padding-top:20px">位置：</td>
            </tr>
            <tr height="25px">
                <td>
                    <select id="type" name="type" class="easyui-combobox"
                            data-options="editable:false,panelHeight:92,width:292,height:25">
                        <option value="1">热映影片</option>
                        <option value="2">即将上映</option>
                        <option value="3">活动</option>
                        <option value="4">影院</option>
                    </select>
                </td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">目标ID(如果使用位置为热映影片,则ID为影片ID,如果使用位置为活动,则ID为活动ID)：</td>
            </tr>
            <tr height="20px">
            	<td>
                    <select id="targetId" name="targetId" class="easyui-combobox"
                            data-options="editable:false,panelHeight:92,width:292,height:25">
                        <#list slist as sequence>
						<option value="${sequence.merge}">${sequence.mname}(${sequence.merge})</option>
						</#list>
                    </select>
                </td>
                <!-- <td><input id="targetId" name="targetId" type="text" class="easyui-textbox"
                           style="width:292px;height:25px"/></td> -->
            </tr>
        </table>
    </form>
</div>
