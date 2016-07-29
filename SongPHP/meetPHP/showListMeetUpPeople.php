<?php
	$con= mysqli_connect();
	
	$ornerID= $_GET['ornerID'];
	$meetName = $_GET['meetName'];
	
	$sql = "SELECT * FROM partyList where MeetName='$meetName' and OrnerID='$ornerID'";
	
	$result = mysqli_query($con,$sql);
	
	while($row = mysqli_fetch_row($result))
{
	echo ':::'.$row[3];
	echo ':::'.$row[5];
	echo '--:--';
}

	mysqli_close($con);
?>	
