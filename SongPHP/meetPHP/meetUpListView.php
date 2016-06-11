<?php
	$con = mysqli_connect(HOST,USER,PASS,DB);
	
	$sql = "SELECT * FROM meetUpList";
	
	$result = mysqli_query($con,$sql);
	
	while($row = mysqli_fetch_row($result))
{
	echo ':::'.$row[1];
	echo ':::'.$row[2];
	echo ':::'.$row[3];
	echo ':::'.$row[4];
	echo ':::'.$row[5];
	echo ':::'.$row[6];
	echo ':::'.$row[7];
	echo ':::'.$row[8];
	echo ':::'.$row[9];
	echo ':::'.$row[10];
	echo '--:--';
}

	mysqli_close($con);
?>	
