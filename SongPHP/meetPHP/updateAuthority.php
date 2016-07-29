<?php

	$meetName = $_GET['meetName'];
	$ornerID = $_GET['ornerID'];
	$userID = $_GET['userID'];
			

	$con=mysqli_connect() or die("접속불가");
	$sql ="UPDATE partyList set authority=1 where MeetName ='$meetName' and OrnerID ='$ornerID' and myUserID='$userID' ";
	if(mysqli_query($con,$sql)) 
	{
		echo 'Success Authority Update';
		mysqli_close($con);

	}
	else echo ' 실패 ';
		
	
		
?>		
