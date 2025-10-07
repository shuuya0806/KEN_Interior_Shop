// カードのカートに追加ボタンと数量変更タブの判定を割けるため分離
document.addEventListener("DOMContentLoaded", function() {
	
	// すべてのカードを取得
  	const item = document.querySelectorAll(".item-card");
	
  	// 各カードにクリックイベントを設定
  	item.forEach(card => {
	  
		// ホバー時にクリック可能っぽくする
    	card.style.cursor = "pointer"; 
    	card.addEventListener("click", function() {
		
		  	// data-url属性から取得
	      	const url = card.dataset.url; 
	      	if(url) {
			  
				// カード全体をクリックしたら遷移
	        	window.location.href = url; 
	      	}
   	 	});
		
		// オプション：ホバーで少し影や色を変える場合
	    card.addEventListener("mouseenter", () => {
	      card.style.backgroundColor = "#FFAD90";
	      
	      // スムーズに変化
	      card.style.transition = "backgroundColor 0.3s";	
	    });
    
    	// 元の色に戻す（空文字でデフォルトに）
	    card.addEventListener("mouseleave", () => {
	      card.style.backgroundColor = "";	
	    });
	});
	
	// すべてのitem-card内の一部を取得
  	const img = document.querySelectorAll(".item-img");

  	// 各カードにクリックイベントを設定
  	img.forEach(card => {
		  
		// ホバー時にクリック可能っぽくする
    	card.style.cursor = "pointer"; 
    	card.addEventListener("click", function() {
			
			// data-url属性から取得
      		const url = card.dataset.url; 
      		
      			// カード全体をクリックしたら遷移
		    	if(url) {
		        	window.location.href = url; 
		      	}
    	});

    	// オプション：ホバーで少し影や色を変える場合
    	card.addEventListener("mouseenter", () => {
      		card.style.backgroundColor = "#FFAD90";
      		
      		// スムーズに変化
      		card.style.transition = "backgroundColor 0.3s";	
    	});
    	
    	// 元の色に戻す（空文字でデフォルトに）
    	card.addEventListener("mouseleave", () => {
      		card.style.backgroundColor = "";	
    	});
  	});
});