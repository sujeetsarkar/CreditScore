<?php
// echo "hi";

require 'connection.php';

$username = $_GET['username'];

$res = mysqli_query($con,"SELECT aadhar_number,c_score from sjbit_user where user_name='$username';");

$row= mysqli_fetch_array($res);
$aadhar = $row[0];
$cur_score = $row[1];


//All variables
$general_usage = 0.1;
$payment_history = 0.3;
$credit_utilisation = 0.25;
$credit_length_history = 0.15;
$new_credit = 0.1;
$credit_mix = 0.1;



// GENERAL USAGES
$res = mysqli_query($con,"SELECT exp_per_month,monthly_incm from sjbit_ac_detail where aadhar_number = '$aadhar';");
$row = mysqli_fetch_array($res)[0];
$exp = $row[0];
$mon = $row[1];
if($exp > 0) 
{
	$var1 = $mon/$exp;

	if($var1 < (1/4))
	{
		$cur_score -= $general_usage * 3;
	}

	else if($var1 < (1/2) && $var1 > (1/4))
	{
		$cur_score += $general_usage * 4; 
	}
	 else
	 {
	 	$cur_score -= $general_usage * 3;
	 }
}

//--------------------------------------------------

 //PAYMENT HISTORY
 $threshold__emi = 30000;
 $pays_at_time = true;
 $res = mysqli_query($con,"SELECT s.emi_per_month, i.due_date, i.pay_date from sjbit_loan as s, sjbit_loan_insmnt_detail as i where s.loan_id = i.loan_id and  ac_number IN(SELECT ac_number from sjbit_ac_detail where aadhar_number = '$aadhar');");
if($res)
{
	if(mysqli_num_rows($res) > 0)
	{
		while($row = mysqli_fetch_array($res))
		{
			$found = false;

			$emi = $row[0]; 
			$due = $row[1];
			$pay = $row[2];
			if($emi > $threshold__emi)
			{	
				$found = true;
				break;
			}

			if($pay < $due)
			{
				$pays_at_time = true;
			}
			else 
				$pays_at_time = false;

			if($found && $pays_at_time)
				$cur_score += $payment_history * 11;
			else if($found && !$pays_at_time)
				$cur_score -= $payment_history * 11;

			if(!$found && $pays_at_time)
				$cur_score += $payment_history * 8;
			else if(!$found && !$pays_at_time)
				$cur_score -= $payment_history * 8;
		}
	}
}
	$res1 = mysqli_query($con,"SELECT credit_id from sjbit_credit where ac_number IN(SELECT ac_number from sjbit_ac_detail where aadhar_number = '$aadhar');");
	if($res1)	
	{
		$row1 = mysqli_fetch_array($res1);
		$c_id = $row1[0];
		if(is_null($c_id) )
		{
			$cur_score -= $payment_history * 6;
		}
		else
		{
			$res = mysqli_query($con,"SELECT due_date,pay_date from credit_inst_detail where credit_id IN(SELECT credit_id FROM sjbit_credit where ac_number IN(SELECT ac_number from sjbit_ac_detail where aadhar_number = '$aadhar'));");
			$row = mysqli_fetch_array($res);
			$due = $row[0];
			$pay = $row[1];
			if($due > $pay)
			{
				$cur_score += $payment_history * 6;
			}
			else
			{
				$cur_score -= $payment_history * 6;
			}

		}
				
	}
	
$res = mysqli_query($con,"SELECT due_date,pay_date from sjbit_water_trans where customer_id IN(SELECT customer_id from sjbit_water_bills where aadhar_number = '$aadhar');");

if($res)
{
		$row = mysqli_fetch_array($res);
		$due = $row[0];
		$pay = $row[1];

		$res1 = mysqli_query($con,"SELECT due_date,pay_date from sjbit_ele_trans where customer_id IN(SELECT customer_id from sjbit_ele_bills where aadhar_number = '$aadhar');");
	if($res1)
	{
		$row1 = mysqli_fetch_array($res);
		$due1 = $row1[0];
		$pay1 = $row1[1];
	}
		if($due > $pay && $due1 > $pay1)
		{
			$cur_score += $payment_history * 4;
		}
		else
		{
			$cur_score -= $payment_history * 4;
		}
	}
//-----------------------------------------------------------

//CREDIT UTILISATION
$res = mysqli_query($con,"SELECT credit_amt, credit_uti_per_month from sjbit_credit where ac_number IN (SELECT ac_number from sjbit_ac_
	detail where aadhar_number = '$aadhar');");
if($res)
{
	$row = mysqli_fetch_array($res);

	$c_amt = $row[0];
	$c_uti = $row[1];
	if($c_amt > 0 && $mon > 0)
	{
		$var1 = $c_uti/$c_amt;
		$var2 = $emi/$mon;
		if($var1 < 0.5)
		{
			$cur_score += $credit_utilisation * 7;
		}
		 else
		 	$cur_score -= $credit_utilisation * 7;

		 if($var2 > 0.5)
		 {
		 	$cur_score -= $credit_utilisation * 6;
		 }
		 else if($var2 < 0.5 && $var2 > 0.25)
		 {
		 	$cur_score += $credit_utilisation * 5;
		 }
		 else
		 	$cur_score -= $credit_utilisation * 4;
	}
}
//-------------------------------------------------------------

 //CREDIT LENGTH HISTORY

$res = mysqli_query($con,"SELECT issue_date from sjbit_loan where ac_number IN(SELECT ac_number from sjbit_ac_detail where aadhar_number = '$aadhar');");
if($res)
{
		$i_date = mysqli_fetch_array($res);
		$x = strtotime('date');
		$y = strtotime('-1 years');

	if($i_date > $y)
	{
		$cur_score += $credit_length_history * 7;
	}
	else
	{
		$cur_score -= $credit_length_history * 7;
	}
}

$res = mysqli_query($con,"SELECT issue_date from sjbit_credit where ac_number IN(SELECT ac_number from sjbit_ac_detail where aadhar_number = '$aadhar');");
if($res)
	{
		$i_date = mysqli_fetch_array($res);
		$x = strtotime('date');
		$y = strtotime('-1 years');
	if($i_date > $y)
	{
		$cur_score += $credit_length_history * 6;
	}
	else
	{
		$cur_score -= $credit_length_history * 6;
	}
 
}
$res = mysqli_query($con,"SELECT  due_date from sjbit_water_trans where customer_id IN(SELECT customer_id from sjbit_water_bills where aadhar_number = '$aadhar');");
if($res)
{
	$due = mysqli_fetch_array($res);
	$x = strtotime('date');
	$y = strtotime('-3 months');
	if($due < $y)
	{
		$cur_score += $credit_length_history * 2;
	}
	else
	{
		$cur_score -= $credit_length_history * 2;
	}

}
//----------------------------------------------------------------------------------------------------------------------------------------------------------
//NEW CREDIT
	$res = mysqli_query($con,"SELECT count(loan_id) from sjbit_loan where ac_number IN(SELECT ac_number from sjbit_ac_detail where aadhar_number = '$aadhar');");
	if($res)
	{
		$no_of_loans = mysqli_fetch_array($res);
		if($no_of_loans > 0 && $no_of_loans <=2)
		{
			$res1 = mysqli_query("SELECT issue_date from sjbit_loan where ac_number IN(SELECT ac_number from sjbit_ac_detail where aadhar_number = '$aadhar');");
			$i_date = mysqli_fetch_array($res1);
			$x = strtotime('date');
			$y = strtotime('-1 years');
			if($i_date < $y)
			{
				$cur_score += $new_credit * 6;
			}
			else
			{
				$cur_score -= $new_credit * 6;
			}
		}
	}
	$res = mysqli_query($con,"SELECT count(credit_id) from sjbit_credit where ac_number IN(SELECT ac_number from sjbit_ac_detail where aadhar_number = '$aadhar');");
	if($res)
	{
		$no_of_loans = mysqli_fetch_array($res);
		if($no_of_loans > 0 && $no_of_loans <=2)
		{
			$res1 = mysqli_query("SELECT issue_date from sjbit_credit where ac_number IN(SELECT ac_number from sjbit_ac_detail where aadhar_number = '$aadhar');");
			$i_date = mysqli_fetch_array($res1);
			$x = strtotime('date');
			$y = strtotime('-1 years');
			if($i_date < $y)
			{
				$cur_score += $new_credit * 4;
			}
			else
			{
				$cur_score -= $new_credit * 4;
			}
		}
	}

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
 //CREDIT MIX
 $found = false;
 $res = mysqli_query($con,"SELECT loan_id from sjbit_loan where ac_number IN(SELECT ac_number from sjbit_ac_detail where aadhar_number = '$aadhar');");

 $res1 = mysqli_query($con,"SELECT credit_id from sjbit_credit where ac_number IN(SELECT ac_number from sjbit_ac_detail where aadhar_number = '$aadhar');");
 if(mysqli_num_rows($res) > 0 && mysqli_num_rows($res1) > 0)
 {
 	$found = true;
 }	
 


if($found)
{
	$cur_score += $credit_mix * 6;
}
 else
  $cur_score -= $credit_mix * 6;
//GOVT SERVICE
 $found = false;
 $res = mysqli_query($con,"SELECT * from sjbit_water_bills where aadhar_number = '$aadhar');");
 
if($res)
	if(mysqli_num_rows($res) > 0)
	 {
	 	$found = true;
	 }
	 else
	 {
	 	$res = mysqli_query($con,"SELECT * from sjbit_ele_bills where aadhar_number = '$aadhar');");
		if($res)
		 	if(mysqli_num_rows($res) > 0)
			 {
			 	$found = true;
			 }	
	 }


if($found)
{
	$cur_score += $credit_mix * 4;
}
 else
 {
  	 $cur_score -= $credit_mix * 4;
  }

//echo

$res = mysqli_query($con, "UPDATE sjbit_user set c_score = '$cur_score' WHERE aadhar_number = '$aadhar';");
if($cur_score < 300)
{
	$cur_score = 300;
}
if($cur_score > 850)
{
	$cur_score = 850;
}

$response["credit_score"] = $cur_score;
echo json_encode($response);

?>