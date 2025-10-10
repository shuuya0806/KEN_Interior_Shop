// ボタンがクリックされたら確認ダイアログを表示
const confirmButton = document.getElementById('test');
confirmButton.addEventListener('click', () => {
  const isConfirmed = confirm('本当に削除しますか？');

  if (isConfirmed) {
    console.log('削除処理を実行しました。');
    // ここに実際の削除処理などを書く
  } else {
    console.log('キャンセルされました。');
  }
});