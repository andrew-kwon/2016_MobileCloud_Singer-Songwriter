<?php

	$UserID=$_GET['UserID'];
	$SongName=$_GET['SongName'];

	$con= mysqli_connect("####","####","####","####");
	
	$sql = "SELECT * FROM comments WHERE UserID ='$UserID' AND SongName='$SongName' ";
	$number=1;
	
	$result = mysqli_query($con,$sql);
	
	while($row = mysqli_fetch_row($result))
{
	echo ':::'.$row[3];
	echo ':::'.$row[4];
	echo '--:--';
	$number++;
}

	mysqli_close($con);
?>	
