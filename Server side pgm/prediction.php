<?php
require 'connection.php';

$uname = $_GET['uname'];

$result = mysqli_query($con, "SELECT aadhar_number,pan_number,c_score FROM users WHERE user_name='$uname' ;");
$penalty=0;
if($result)
{
$r = mysqli_fetch_array($result);
$ano=$r[0];
$pno=$r[1];
$c_score=$r[2];
$q= mysqli_query($con, "SELECT trans_id FROM ele_bills WHERE aadhar_number='$ano';");
}

if($result)
{
    $r=mysqli_fetch_array($result);
    $id=$r[0];
    $result = mysqli_query($con, "SELECT due_date,pay_date FROM ele_trans WHERE trans_id= '$id';");
}
if($result)
    if(mysqli_num_rows($result)>0)
    	{
            $r=mysqli_fetch_array($result);
            $due_date=$r[0];
            $pay_date=$r[1];
            $d=($p_date-$due_date);

            if($d<10)
    	    {
                $penalty=1;
            }
            else if($d>=10 && $d<=20)
            {
                $penalty=2;
            }
            else if($d>20 && $d<=30)
            {
    	        $penalty=3;
            }
        }

$result = mysqli_query($con, "SELECT due_date,pay_date FROM water_trans WHERE trans_id= '$id';");
if($result)
{
    if(mysqli_num_rows($result)>0)
    	{
            $r=mysqli_fetch_array($result);
            $due_date=$r[0];
            $pay_date=$r[1];
            $d=($p_date-$due_date);

            if($d<10)
    	    {
                 $penalty=($penalty+1);
            }
            else if($d>=10 && $d<=20)
            {
                $penalty=($penalty+2);
            }
            else if($d>20 && $d<=30)
            {
            	$penalty=($penalty+3);
            }
    }
}

$result = mysqli_query($con, "SELECT emi,p_date,due_date FROM credit WHERE pan_number='$pno' ;");
if($result)
{
    if(mysqli_num_rows($result)>0)
    	{
            $r=mysqli_fetch_array($result);
            $emi=$r[0];
            $p_date=$r[1];
            $due_date=$r[2];
            $d=($p_date-$due_date);
       if($d<10)
    	    {
                $penalty=($penalty+1);
            }
            else if($d>=10 && $d<=20)
            {
                $penalty=($penalty+2);
            }
            else if($d>20 && $d<=30)
            {
            	$penalty=($penalty+3);
            }
    }
}

$p_score1=($c_score-450)/(2+$penalty);
$p_score1= ceil($p_score1);
$p_score1= ($c_score+($p_score1*(1+$penalty)));

if($p_score1<300)
     	$p_score1=300;
     else if($p_score1>850)
     	$p_score1=850;
     else;

$response["x-axis"]["0"]="nov16";
$response["y-axis"]["0"]=$p_score1;

$p_score2=($p_score1-450)/(3+$penalty);
$p_score2= ceil($p_score2);
$p_score2= ($p_score1+($p_score2*(1+$penalty)));

if($p_score2<300)
     	$p_score2=300;
     else if($p_score2>850)
     	$p_score2=850;
     else;

$response["x-axis"]["1"]="dec16";
$response["y-axis"]["1"]=$p_score2;

$p_score3=($p_score2-450)/(4+$penalty);
$p_score3= ceil($p_score3);
$p_score3= ($p_score2+($p_score3*(1+$penalty)));

if($p_score3<300)
     	$p_score3=300;
     else if($p_score3>850)
     	$p_score3=850;
     else;

$response["x-axis"]["2"]="jan17";
$response["y-axis"]["2"]=$p_score3;

$p_score4=($p_score3-450)/(5+$penalty);
$p_score4= ceil($p_score4);
$p_score4= ($p_score3+($p_score4*(1+$penalty)));

if($p_score4<300)
     	$p_score4=300;
     else if($p_score4>850)
     	$p_score4=850;
     else;

$response["x-axis"]["3"]="feb17";
$response["y-axis"]["3"]=$p_score4;

$p_score5=($p_score4-450)/(6+$penalty);
$p_score5= ceil($p_score5);
$p_score5= ($p_score4+($p_score5*(1+$penalty)));

if($p_score5<300)
     	$p_score5=300;
     else if($p_score5>850)
     	$p_score5=850;
     else;

$response["x-axis"]["4"]="mar17";
$response["y-axis"]["4"]=$p_score5;

$p_score6=($p_score5-450)/(7+$penalty);
$p_score6= ceil($p_score6);
$p_score6= ($p_score5+($p_score6*(1+$penalty)));

if($p_score6<300)
     	$p_score6=300;
     else if($p_score6>850)
     	$p_score6=850;
     else;

$response["x-axis"]["5"]="apr17";
$response["y-axis"]["5"]=$p_score6;

$p_score7=($p_score6-450)/(8+$penalty);
$p_score7= ceil($p_score7);
$p_score7= ($p_score6+($p_score7*(1+$penalty)));

if($p_score7<300)
     	$p_score7=300;
     else if($p_score7>850)
     	$p_score7=850;
     else;

$response["x-axis"]["6"]="may17";
$response["y-axis"]["6"]=$p_score7;

$p_score8=($p_score7-450)/(9+$penalty);
$p_score8= ceil($p_score8);
$p_score8= ($p_score7+($p_score8*(1+$penalty)));

if($p_score8<300)
     	$p_score8=300;
     else if($p_score8>850)
     	$p_score8=850;
     else;

$response["x-axis"]["7"]="jun17";
$response["y-axis"]["7"]=$p_score8;

$p_score9=($p_score8-450)/(10+$penalty);
$p_score9= ceil($p_score9);
$p_score9= ($p_score8+($p_score9*(1+$penalty)));

if($p_score9<300)
     	$p_score9=300;
     else if($p_score9>850)
     	$p_score9=850;
     else;

$response["x-axis"]["8"]="jul17";
$response["y-axis"]["8"]=$p_score9;

$p_score10=($p_score9-450)/(11+$penalty);
$p_score10= ceil($p_score10);
$p_score10= ($p_score9+($p_score10*(1+$penalty)));

if($p_score10<300)
     	$p_score10=300;
     else if($p_score10>850)
     	$p_score10=850;
     else;

$response["x-axis"]["9"]="aug17";
$response["y-axis"]["9"]=$p_score10;

$p_score11=($p_score10-450)/(12+$penalty);
$p_score11= ceil($p_score11);
$p_score11= ($p_score10+($p_score11*(1+$penalty)));

if($p_score11<300)
     	$p_score11=300;
     else if($p_score11>850)
     	$p_score11=850;
     else;

$response["x-axis"]["10"]="sep17";
$response["y-axis"]["10"]=$p_score11;

$p_score12=($p_score11-450)/(13+$penalty);
$p_score12= ceil($p_score12);
$p_score12= ($p_score11+($p_score12*(1+$penalty)));

if($p_score12<300)
     	$p_score12=300;
     else if($p_score12>850)
     	$p_score12=850;
     else;

$response["x-axis"]["11"]="oct17";
$response["y-axis"]["11"]=$p_score12;

echo json_encode($response);
?>