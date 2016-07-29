<?php
	$con= mysqli_connect();
	$myUserID = $_GET['myUserID'];	
	$sql = "SELECT MeetName, OrnerID, authority FROM partyList where myUserID='$myUserID'";
	
	$result = mysqli_query($con,$sql);
	
	while($row = mysqli_fetch_row($result))
{
	$joinSql ="select * from meetUpList where meetName='$row[0]' and ornerID='$row[1]'";
	$joinResult=mysqli_query($con,$joinSql);
	while($joinRow=mysqli_fetch_row($joinResult))
	{
		echo ':::'.$joinRow[1];
		echo ':::'.$joinRow[2];
		echo ':::'.$joinRow[3];
		echo ':::'.$joinRow[4];
		echo ':::'.$joinRow[5];
		echo ':::'.$joinRow[6];
		echo ':::'.$joinRow[7];
		echo ':::'.$joinRow[8];
		echo ':::'.$joinRow[9];
		echo ':::'.$joinRow[10];
		echo ':::'.$row[2];
		echo '--:--';
	}
}

	mysqli_close($con);
?>	
