// ページの読み込みが完了したら処理を開始
document.addEventListener("DOMContentLoaded", function () {
  // 郵便番号入力フィールドを取得
  const postalInput = document.getElementById("postal");

  // 郵便番号フィールドが存在する場合のみ処理を続行
  if (postalInput) {
    // フィールドからフォーカスが外れたときにイベント発火
    postalInput.addEventListener("blur", function () {
      // 入力された郵便番号からハイフンを除去し、前後の空白を削除
      const postalCode = postalInput.value.replace("-", "").trim();

      // 郵便番号が7桁の半角数字であるかをチェック
      if (!postalCode || postalCode.length !== 7 || !/^\d{7}$/.test(postalCode)) {
        return; // 不正な形式なら処理しない
      }

      // zipcloud APIを使って住所情報を取得
      fetch(`https://zipcloud.ibsnet.co.jp/api/search?zipcode=${postalCode}`)
        .then(response => response.json()) // レスポンスをJSON形式に変換
        .then(data => {
          console.log("APIレスポンス:", data); // デバッグ用ログ

          // 結果が存在する場合は住所情報を取得して各フィールドに反映
          if (data.results && data.results.length > 0) {
            const result = data.results[0];

            // 都道府県・市区町村・番地の入力フィールドを取得
            const prefecture = document.getElementById("prefecture");
            const city = document.getElementById("city");
            const street = document.getElementById("street");

            // APIから取得した値をそれぞれのフィールドにセット
            if (prefecture) prefecture.value = result.address1;
            if (city) city.value = result.address2;
            if (street) street.value = result.address3;

            // 以前のエラーメッセージがあれば削除
            const oldError = document.getElementById("postal-error");
            if (oldError) oldError.remove();
          } else {
            // APIから住所が取得できなかった場合のエラーメッセージ（重複表示防止）
            if (!document.getElementById("postal-error")) {
              const errorMsg = document.createElement("div");
              errorMsg.id = "postal-error";
              errorMsg.textContent = "住所が見つかりませんでした。";
              errorMsg.style.color = "red";
              postalInput.parentNode.appendChild(errorMsg);
            }
          }
        })
        .catch(() => {
          alert("住所検索に失敗しました。"); // 通信エラーなどが発生した場合
        });
    });
  }
});