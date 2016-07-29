<?php

	$meetName = $_GET['meetName'];
	$ornerID = $_GET['ornerID'];
	$meetDate = $_GET['meetDate'];
	$meetTime = $_GET['meetTime'];

			

	$con=mysqli_connect() or die("접속불가");
	$sql ="UPDATE meetUpList set meetDate = '$meetDate',meetTime ='$meetTime' where meetName ='$meetName' AND ornerID ='$ornerID'";
	if(mysqli_query($con,$sql)) 
	{
		echo 'Success Time Update';
		mysqli_close($con);

	}
	else echo ' 실패 ';
		
	
		
?>		
