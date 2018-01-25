<div style="margin: 20px 5px">
	<form name="theform" method="post" id="theform">
		<table style="font-size: 12px; border-spacing: 20px 0px">
			<tr height="20px">
				<td>影院：</td>
			</tr>
			<tr height="25px">
				<td><select id="cid" name="cid" class="easyui-combobox" multiple="multiple"
					data-options="editable:false,panelHeight:120,width:292,height:25">
					<option value="all">全选</option>
						<#list cinemaList as cinema>
							<option value="${cinema.cid}" <#if cinema.cid == cid>selected</#if> >${cinema.cname}</option> 
						</#list>
				</select></td>
			</tr>

			<tr height="20px">
				<td style="padding-top: 20px">卖品：</td>
			</tr>
			<tr height="25px">
				<td><select id="info" name="info" class="easyui-combobox" 
					data-options="editable:false,panelHeight:120,width:292,height:25">
						<#list concessionList as concession>
						<option value="${concession.pid}:${concession.name}:${concession.price}">名称：${concession.name},价格：${concession.price / 100}</option>
						</#list>
				</select></td>
			</tr>

			<tr height="20px">
				<td style="padding-top: 20px">类型：</td>
			</tr>
			<tr height="25px">
				<td><select id="ptype" name="ptype" class="easyui-combobox"
					data-options="editable:false,panelHeight:92,width:292,height:25">
						<option value="0">小食卖品</option>
						<option value="1">充值卖品</option>
				</select></td>
			</tr>

		</table>
	</form>
</div>
