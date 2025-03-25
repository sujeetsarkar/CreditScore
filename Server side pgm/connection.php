<?php
//ip:172.20.150.100
$server="localhost";
$user="root";
$password="";
$db_name="sjbit_project";
$con= @mysqli_connect($server, $user, $password, $db_name);
if(!$con)
{
	echo "error in connction";
}

?>