<?php
require 'connection.php';

$uname = $_GET['uname'];
$passwd = $_GET['passwd'];

$result = mysqli_query($con, "SELECT email FROM sjbit_user WHERE user_name='$uname' AND passwd='$passwd';");

	if(mysqli_num_rows($result)>0)
	{
		$e=mysqli_fetch_array($result);
		$response["email"]=$e[0];
		$response["username"]=$uname;
		$response["success"]= "1";
		$response["message"] = "Successfully SignedIn";
	} 
    else
    {
        $response["success"] = "0";
		$response["message"] = "Incorrect Username or Password";
    }

    echo json_encode($response);
?>