document.addEventListener("DOMContentLoaded", function () {
  const usePointYes = document.getElementById("yes");
  const usePointNo = document.getElementById("no");
  const pointValueWrapper = document.getElementById("pointValueWrapper");
  const pointValueInput = document.getElementById("pointValue");
  const pointError = document.getElementById("pointError");

  // data-point 属性から現在ポイントを取得（数値化）
  const currentPoint = parseInt(document.getElementById("currentPoint").dataset.point, 10);
  console.log("currentPoint", currentPoint);

  function togglePointInput() {
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

    // 数字チェック
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

  usePointYes.addEventListener("change", togglePointInput);
  usePointNo.addEventListener("change", togglePointInput);
  pointValueInput.addEventListener("input", validatePointInput);

  document.getElementById("order-check").addEventListener("submit", function (e) {
    if (usePointYes.checked && !validatePointInput()) {
      e.preventDefault();
    }
  })

  togglePointInput();
});
