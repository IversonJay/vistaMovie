<div style="margin:20px 5px">
    <form name="theform" method="post" id="theform">
        <input name="key" id="key" value="${ticketInfo.key}" type="hidden"/>
        <table style="font-size: 12px;border-spacing:20px 0px">
            <tr height="20px">
                <td>票类名称：</td>
            </tr>
            <tr height="25px">
                <td><input id="name" name="name" type="text" class="easyui-textbox" value="${ticketInfo.name}"
                           data-options="required:true,missingMessage:'票类名称不能为空',validType:'length[2,15]',invalidMessage:'票类名称需在2到15位之间'"
                           style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">服务费(元)：</td>
            </tr>
            <tr height="20px">
                <td><input id="bookingFee" name="bookingFee" type="text" class="easyui-textbox" value="${ticketInfo.bookingFee}"
                           data-options="required:true,missingMessage:'服务费不能为空'"
                           style="width:292px;height:25px"/></td>
            </tr>
            <tr height="20px">
                <td style="padding-top:20px">票类：</td>
            </tr>
            <tr height="25px">
                <td>
                    <select id="info" name="info" class="easyui-combobox"
                            data-options="editable:false,panelHeight:120,width:292,height:25">
                        <#list ticketList as ticket>
                        	<option value="HOPK:${ticket.hopk},${ticket.description}"  <#if ticketInfo.hopk == ticket.hopk>selected</#if>   >HOPK:${ticket.hopk},${ticket.description}</option>
                        </#list>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
