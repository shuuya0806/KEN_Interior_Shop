document.addEventListener('DOMContentLoaded', () => {
	
	// ヘッダーを取得
    const header = document.getElementById('header');
	
	// マウス移動時イベントを設定
    document.addEventListener('mousemove', (e) => {
		
		// 画面上部50px以内にマウスがある場合
        if (e.clientY <= 50) { 
			
			// ヘッダーに "show" クラスを付与（表示するスタイルを適用する想定）
            header.classList.add('show');
        } else {
			
			// 50pxより下にマウスがある場合は "show" クラスを削除（非表示に戻す）
            header.classList.remove('show');
        }
    });
});