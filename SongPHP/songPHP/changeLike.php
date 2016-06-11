
<?php
	     
	$likeUser=$_GET['UserID']; 
	$con = mysqli_connect(HOST,USER,PASS,DB);
 

	$sql = "select SongName, UserID from songLike WHERE likeUserID='$likeUser'";
	$number=1;

	$result = mysqli_query($con,$sql); 
	$boardCon=mysqli_connect(HOST,USER,PASS,SongDB);	
	while($row = mysqli_fetch_row($result))
	{
		$songSql="select * from songDB WHERE songName='$row[0]' and userID='$row[1]'";
		$songResult=mysqli_query($boardCon,$songSql);
		while($songRow=mysqli_fetch_row($songResult))
		{
			
			echo ':::'.$songRow[1];
			echo ':::'.$songRow[2];
			echo ':::'.$songRow[3];
			echo ':::'.$songRow[4];
			echo ':::'.$songRow[5];
			echo ':::'.$songRow[6];
			echo ':::'.$songRow[7];
			echo '--:--';
		}
		
	}
 	mysqli_close($boardCon);
	mysqli_close($con);
?>
