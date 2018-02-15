<STYLE type="text/css">

	.mennu a {
	font-family: Geneva, Arial, Helvetica, sans-serif;
	font-size: 20px;
	font-weight: bold;
	color: #5F5FE1;
	text-decoration: none;
	}
  	.mennu a:hover {
	FONT-WEIGHT: bold; 
	FONT-SIZE: 20px; 
	COLOR: #FFA125; 
	FONT-FAMILY: Geneva, Arial, Helvetica, sans-serif; 
	TEXT-DECORATION: underline
	}
	.mennu TABLE {
	FONT-SIZE: 20px;
	FONT-WEIGHT: bold;
	}
	.mennu .campo {
	Background-color: white; 
	bgColor: white; 
	align: center;
	text-align: center;
	align: center;
	}
	
 </STYLE>
<script type="text/javascript">
function administraGrupos(){
    window.document.forma.command.value='administraGrupos';
    window.document.forma.submit();
}
function administraClientes(){
    window.document.forma.command.value='administraClientes';
    window.document.forma.submit();
}
</script>
<div class="mennu">
	<TABLE cellSpacing=0 cellPadding=0 width="70%" align="center">
	<TR>
	    <TD bgColor="#009861">
	      <TABLE cellSpacing=1 cellPadding=5 width="100%" border=0 >
		      <TR>
				<TD class=campo ><a href="#" onClick="administraGrupos()"> Grupos Micro</a></TD>
				<TD class=campo ><a href="#" onClick="administraClientes()"> Clientes Micro</a></TD>
			 </TR>
		  </table>
		</TD>
	</table>
</div>