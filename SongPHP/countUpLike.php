<?php

	$myUserID = $_GET['myUserID'];
	$SongName = $_GET['SongName'];
	$UserID =$_GET['UserID'];

			

	$con=mysqli_connect("####","####","####","####") or die("접속불가");

	$sql = "SELECT * FROM songLike WHERE SongName='$SongName' AND UserID='$UserID' AND likeUserID ='$myUserID' ";
			
	$check = mysqli_fetch_array(mysqli_query($con,$sql));
			
	if(isset($check)){
		echo '이미 좋아요 하셨습니다.!';
	}
	else{				
		$sql = "INSERT INTO songLike (SongName,UserID,likeUserID) VALUES('$SongName','$UserID','$myUserID')";
		if(mysqli_query($con,$sql)){
			echo 'Success';
		mysqli_close($con);
		$con2 = mysqli_connect("####","####","####","####") or die("singer 접속불가");
		$sql2 ="UPDATE songDB set likeCount = (likeCount +1) where SongName ='$SongName' AND UserID ='$UserID'";
			if(mysqli_query($con2,$sql2)) {echo 'Success';
				mysqli_close($con2);
			}
			else echo ' 실패 ';
		}
	}
		
?>		
