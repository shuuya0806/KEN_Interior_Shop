// staffOrderHistoryの「注文取消」ボタンが押された時の処理
const confirmButton = document.getElementById('cancel');

//「注文取消」ボタンが表示されたら、確認メッセージを表示
confirmButton.addEventListener('click', () => {
  const isConfirmed = confirm('本当に削除しますか？');

  if (isConfirmed) {
	//「はい」が選択された場合の処理
	// 注文取消の対象となるorder_idをHTMLから取得
	const orderId = confirmButton.value;
	
	// staffOrderControllerにデータを送信  
	fetch('/stafforder/cancel',
		{
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({ orderId: orderId }) //order_idを引数として渡す処理
		})
		// 以降、エラー処理
		.then((response) => {
		    if (!response.ok) {
		        throw new Error();
		    }
		    return response.text(); // あるいは response.json()
		})
		.then((result) => {
			console.log('削除処理を実行しました。');
			alert('削除完了しました');
			location.reload(); //ページを再読み込み
		})
		.catch((error) => {
		    console.error('削除エラー:', error);
		    alert('削除中にエラーが発生しました。');
		})
	// 処理完了時の部分
	console.log('削除処理を実行しました。');
  } else {
	// キャンセルした場合
    console.log('キャンセルされました。');
  }
});