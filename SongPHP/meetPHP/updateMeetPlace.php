<?php

	$meetName = $_GET['meetName'];
	$ornerID = $_GET['ornerID'];
	$placeLati = $_GET['latitude'];
	$placeLongti = $_GET['longitude'];
	$placeName = $_GET['placeName'];

			

	$con=mysqli_connect() or die("접속불가");
	$sql ="UPDATE meetUpList set placeLati = '$placeLati', placeLongti='$placeLongti',meetPlaceName='$placeName' where meetName ='$meetName' AND ornerID ='$ornerID'";
	if(mysqli_query($con,$sql)) 
	{
		echo 'Success Update Place';
		mysqli_close($con);
	}
	else echo ' 실패 ';
		
	
		
?>		
