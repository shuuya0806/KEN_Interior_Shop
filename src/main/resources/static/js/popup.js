window.addEventListener("load", function() {
	
	// body 要素を取得
  	const body = document.querySelector("body");
  	
  	// メッセージと背景色を取得
  	const message = body.dataset.message;
  	const bgColor = body.dataset.color;


  	if (message && bgColor) {
		
		// ポップアップ用の div 要素を作成
    	const popup = document.createElement("div");
    	popup.innerText = message;

		// CSSスタイル
    	Object.assign(popup.style, {
      		position: "fixed",
      		top: "50%",
      		left: "50%",
	      	transform: "translate(-50%, -50%)",
	      	background: bgColor,
	      	color: "#fff",
	      	padding: "20px 40px",
	      	borderRadius: "10px",
	      	fontSize: "1.2rem",
	      	textAlign: "center",
	      	boxShadow: "0 4px 12px rgba(0,0,0,0.3)",
	      	zIndex: "10000",
	      	opacity: "0",
	      	transition: "opacity 0.5s"
   		 });
		
		// body に追加
    	document.body.appendChild(popup);

    	// フェードイン
    	setTimeout(() => popup.style.opacity = "1", 10);

    	// 1秒後にフェードアウトして削除
    	setTimeout(() => {
      		popup.style.opacity = "0";
      		setTimeout(() => popup.remove(), 500);
    	}, 1000);
  	}
  
});