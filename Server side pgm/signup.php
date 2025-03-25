<?php
require 'connection.php';

function randomPassword(){
    	$alpha="abcdenghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    	$pass=array();
    	for($i=0;$i<8;$i++){
    		$n=rand(0,strlen($alpha)-1);
    		$pass[$i]=$alpha[$n];
    	}
    	return implode($pass);
    }

$uname = $_GET['uname'];
$ano = $_GET['ano'];
$pno = $_GET['pno'];
$mob = "null";
$email= "null";
$c_score = 350;
$p_score = 350;
$passwd = randomPassword();
$res = mysqli_query($con, "SELECT email, mobile from sjbit_aadhar where aadhar_number='$ano';");

    if(mysqli_num_rows($res)>0)
    {
        $e=mysqli_fetch_array($res);
        $email = $e[0];
        $mob = $e[1];
    }

    $result = mysqli_query($con, "
	INSERT INTO sjbit_user (user_name,passwd,aadhar_number,pan_number,mobile,email,c_score,pridicted_c_score) SELECT '$uname','$passwd','$ano','$pno','$mob','$email','$c_score','$p_score' ;");

	if($result)
	{
		$response["success"]= "1";
		$response["message"] = "Signed up Successfully";
		$response["email"] = $email;
		$response["mobile"] = $mob;
	} 
    else
    {
        $response["success"] = "0";
		$response["message"] = "error";
    }

    echo json_encode($response);
?>