// ボタンがクリックされたら確認ダイアログを表示
const confirmButton = document.getElementById('cancel');
confirmButton.addEventListener('click', () => {
  const isConfirmed = confirm('本当に削除しますか？');

  if (isConfirmed) {
	fetch('/stafforder/cancel',
		{
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({})
		})
		.then((response) => {
		    if (!response.ok) {
		        throw new Error();
		    }
		    return response.text(); // あるいは response.json()
		})
		.then((result) => {
			console.log('削除処理を実行しました。');
		})
		.catch((reason) => {
		    // エラー
		})
	console.log('削除処理を実行しました。');
  } else {
    console.log('キャンセルされました。');
  }
});