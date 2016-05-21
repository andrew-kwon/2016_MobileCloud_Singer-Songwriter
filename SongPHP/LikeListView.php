
<?php
	define('DB','songLikeDB');
	
	$likeUser=$_GET['UserID']; 
	$con = mysqli_connect(HOST,USER,PASS,DB);
 

	$sql = "select * from songLike WHERE likeUserID='$likeUser'";
	$number=1;

	$result = mysqli_query($con,$sql); 
	while($row = mysqli_fetch_row($result))
	{
	echo ':::'.$row[1];
	echo ':::'.$row[2];
	echo '--:--'; 
	$number++;
	}
 
	mysqli_close($con);
?>
