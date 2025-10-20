//HTML上から必要な情報を取得し、ページ読み込み完了時に起こるイベントを作成
document.addEventListener("DOMContentLoaded", function () {
  const usePointYes = document.getElementById("yes"); //「ポイントを使用する」(ラジオボタン)の状態
  const usePointNo = document.getElementById("no"); //「使用しない」(ラジオボタン)の状態
  const pointValueWrapper = document.getElementById("pointValueWrapper"); //使用するポイント入力欄の表示切替に使用
  const pointValueInput = document.getElementById("pointValue"); //入力された使用するポイントを取得
  const pointError = document.getElementById("pointError"); //エラーメッセージ表示に使用
  const currentPoint = document.getElementById("currentPoint"); //現在のポイントを取得

// 「ポイントを使用する」(ラジオボタン)が押された場合のみ、使用するポイント入力欄を表示する処理
  function togglePointInput() {
	//「ポイントを使用する」が押された場合
    if (usePointYes.checked) {
      pointValueWrapper.style.display = "block";
      pointValueInput.disabled = false;
    } else {
      pointValueWrapper.style.display = "none";
      pointValueInput.disabled = true;
      pointValueInput.value = "";
      pointError.style.display = "none";
      pointError.textContent = "";
    }
  }

  function validatePointInput() {
    const inputValue = pointValueInput.value.trim();

    // 数値かどうかチェック
    if (!/^\d+$/.test(inputValue)) {
      pointError.style.display = "block";
      pointError.textContent = "数字のみ入力してください。";
      return false;
    }

    const parsedValue = parseInt(inputValue, 10);

    if (parsedValue < 1) {
      pointError.style.display = "block";
      pointError.textContent = "1以上のポイントを入力してください。";
      return false;
    }

    if (parsedValue > currentPoint) {
      pointError.style.display = "block";
      pointError.textContent = `使用可能なポイントは ${currentPoint} ポイントまでです。`;
      return false;
    }

    pointError.style.display = "none";
    pointError.textContent = "";
    return true;
  }

  // イベント設定
  usePointYes.addEventListener("change", togglePointInput);
  usePointNo.addEventListener("change", togglePointInput);
  pointValueInput.addEventListener("input", validatePointInput);

  // フォーム送信前のチェック
  document.getElementById("order-check").addEventListener("submit", function (e) {
    if (usePointYes.checked && !validatePointInput()) {
      e.preventDefault();
    }
  });

  // 初期状態の表示切り替え
  togglePointInput();
});
