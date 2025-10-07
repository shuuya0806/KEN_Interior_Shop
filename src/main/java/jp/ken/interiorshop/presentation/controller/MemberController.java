package jp.ken.interiorshop.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jp.ken.interiorshop.application.service.LoginService;
import jp.ken.interiorshop.application.service.RegistService;
import jp.ken.interiorshop.application.service.UpdateService;
import jp.ken.interiorshop.application.service.WithdrawService;
import jp.ken.interiorshop.common.validatior.groups.ValidGroupOrder;
import jp.ken.interiorshop.presentation.form.MemberLoginForm;
import jp.ken.interiorshop.presentation.form.MemberRegistForm;

@Controller
public class MemberController {

	private final LoginService loginService;
	private final RegistService registService;
	private final WithdrawService withdrawService;
	private final UpdateService updateService;

	public MemberController(LoginService loginService, RegistService registService,
	                        WithdrawService withdrawService, UpdateService updateService) {
		this.loginService = loginService;
		this.registService = registService;
		this.withdrawService = withdrawService;
		this.updateService = updateService;
	}

	/**
	 * ログイン画面の表示
	 */
	@GetMapping("/login")
	public String getLoginForm(Model model) {
		
		model.addAttribute("loginUser", new MemberLoginForm());
		
		// ログイン画面へ遷移
		return "memberLogin";
	}

	/**
	 * ログイン処理（バリデーション＋認証）
	 */
	@PostMapping("/login")
	public String doLogin(@Valid @ModelAttribute("loginUser") MemberLoginForm form,
	                      BindingResult result, Model model, HttpSession session) throws Exception {
		
		// 未入力チェック
		if (result.hasErrors()) {
			
			// 入力エラーがある場合はログイン画面に戻る
			return "memberLogin";
		}

		// 認証処理
		MemberLoginForm loginForm = loginService.doLogin(form);
		
		if (loginForm != null) {
			
			// 認証成功：セッションにユーザー情報を保存
			session.setAttribute("loginUser", loginForm);
			Object backUrl = session.getAttribute("currentUrl");
			
			// ログイン用ポップアップ情報をセッションに格納
			session.setAttribute("popupMessage", "ログインしました");
	        session.setAttribute("popupColor", "rgba(40, 167, 69, 0.8)"); // 緑色
			
	        // 元の画面へリダイレクト
			return "redirect:" + backUrl;
		} else {
			
			// 認証失敗：エラーメッセージ表示
			model.addAttribute("loginError", "メールアドレスまたはパスワードが正しくありません");
			
			// ログイン画面へ遷移
			return "memberLogin";
		}
	}

	/**
	 * 「戻る」ボタン押下時の処理（元の画面へ戻る）
	 */
	@GetMapping("/back")
	public String back(HttpSession session) {
		
		// 「戻る」用URLを取得
		Object backUrl = session.getAttribute("currentUrl");
		
		// 取得したURLへリダイレクト
		return "redirect:" + backUrl;
	}

	/**
	 * ログアウト処理（セッション破棄）
	 */
	@GetMapping("/logout")
	public String doLogout(HttpServletRequest request) {
		
		// 古いセッションを破棄
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 新しいセッションを作成してログアウト用ポップアップ情報を格納
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("popupMessage", "ログアウトしました");
        newSession.setAttribute("popupColor", "rgba(0, 123, 255, 0.8)"); // 青色
        
        // 商品一覧へリダイレクト
		return "redirect:/item";
	}

	/**
	 * 会員登録画面の表示
	 */
	@GetMapping("/registration")
	public String toRegist(@ModelAttribute MemberRegistForm memberRegistForm, Model model) {
		
		if (memberRegistForm == null) {
			model.addAttribute("memberRegistForm", new MemberRegistForm());
		}
		
		// 会員登録画面へ遷移
		return "regist";
	}

	/**
	 * 会員登録内容の確認画面表示
	 */
	@PostMapping("/registration")
	public String showRegist(@Validated(ValidGroupOrder.class) @ModelAttribute MemberRegistForm memberRegistForm,
	                         BindingResult rs, Model model) {
		
		// 入力された会員情報をモデルに格納
		model.addAttribute("memberRegistForm", memberRegistForm);
		
		//未入力チェック
		if (rs.hasErrors()) {
			
			// 登録画面へ遷移
			return "regist";
		}
		
	    // メール重複チェック
	    if (registService.isMailDuplicated(memberRegistForm.getMail())) {
	        model.addAttribute("mailError", "このメールアドレスはすでに登録されています");
	        
	        // 登録画面へ遷移
	        return "regist";
	    }
	    
	    // 電話番号重複チェック
	    if (registService.isPhoneNumberDuplicated(memberRegistForm.getPhoneNumber())){
	        model.addAttribute("phoneError", "この電話番号はすでに登録されています");
	        
	        // 登録画面へ遷移
	        return "regist";
	    }
	    
	    // 会員登録内容の確認画面へ遷移
		return "registConfirm";
	}

	/**
	 * 会員情報のDB登録処理
	 */
	@PostMapping("/registDB")
	public String registMembers(@ModelAttribute MemberRegistForm memberRegistForm, Model model) throws Exception {
		
		// DB登録
		registService.registMembers(memberRegistForm); 
		
		// 会員登録完了画面へ遷移
		return "registComplete";
	}

	/**
	 * マイページ表示（ログイン済みユーザー情報の取得）
	 */
	@GetMapping("/mypage")
	public String showMyPage(HttpSession session, Model model) throws Exception {
		
		// セッションからログイン情報を取得
		MemberLoginForm loginUser = (MemberLoginForm) session.getAttribute("loginUser");
		
		if (loginUser == null) {
			
			// 未ログインの場合はログイン画面へリダイレクト
			return "redirect:/login";
		}
		
		// 会員情報を取得しセッションとモデルに格納
		MemberRegistForm memberData = loginService.getLoginData(loginUser.getMemberId());
		session.setAttribute("memberData", memberData);
		model.addAttribute("memberData", memberData);
		
		// マイページ画面へ遷移
		return "mypage";
	}

	/**
	 * 退会確認画面の表示
	 */
	@GetMapping("/withdraw")
	public String showWithdraw() {
		
		//退会確認画面へ遷移
		return "withdraw";
	}

	/**
	 * 退会処理（cancelフラグ更新＋セッション破棄）
	 */
	@PostMapping("/withdraw")
	public String doWithdraw(@ModelAttribute("loginUser") MemberLoginForm form,
	                         HttpSession session, SessionStatus status, Model model) throws Exception {
		
		// 退会処理
		int numberOfRow = withdrawService.withdrawMember(form);
		
		
		if (numberOfRow == 0) {
			model.addAttribute("error", "退会に失敗しました");
			
			// 退会確認画面へ遷移
			return "withdraw";
		}
		
		// セッションの情報を破棄
		status.setComplete();
		session.invalidate();
		
		// 退会完了画面へ遷移
		return "withdrawComplete";
	}

	/**
	 * 会員情報編集画面の表示（既存情報をフォームにセット）
	 */
	@GetMapping("/edit")
	public String showEditPage(@SessionAttribute("memberData") MemberRegistForm memberData,
	                           Model model, HttpSession session) {
		
		// ログインしている会員の情報をモデルに格納
		model.addAttribute("memberRegistForm", memberData);
		
		// 会員情報編集画面へ遷移
		return "edit";
	}

	/**
	 * 編集内容の確認画面表示
	 */
	@PostMapping("/editConfirm")
	public String showEditConfirm(
	        @Validated(ValidGroupOrder.class) @ModelAttribute("memberRegistForm") MemberRegistForm form,
	        BindingResult bindingResult, Model model) {

	    // 未入力チェック
	    if (bindingResult.hasErrors()) {
	    	
	    	// 会員情報編集画面へ遷移
	        return "edit";
	    }

	    // メールアドレスの重複チェック（自分自身を除外）
	    if (updateService.isMailDuplicated(form.getMail(), form.getMemberId())) {
	        model.addAttribute("mailError", "このメールアドレスはすでに他の会員に使用されています");
	        
	        // 会員情報編集画面へ遷移
	        return "edit";
	    }

	    // 電話番号の重複チェック（自分自身を除外）
	    if (updateService.isPhoneNumberDuplicated(form.getPhoneNumber(), form.getMemberId())) {
	        model.addAttribute("phoneError", "この電話番号はすでに他の会員に使用されています");
	        
	        // 会員情報編集画面へ遷移
	        return "edit";
	    }

	    // 編集した情報をモデルに格納
	    model.addAttribute("confirmForm", form);
	    
	    // 会員情報確認画面へ遷移
	    return "editConfirm";
	}

	/**
	 * 編集内容の登録処理（更新＋セッション情報更新）
	 */
	@PostMapping("/editComplete")
	public String completeEdit(@Valid @ModelAttribute("confirmForm") MemberRegistForm form,
	                           Model model, HttpSession session) throws Exception {

		// 会員情報の更新処理
		updateService.updateMember(form);

		// ヘッダー表示用のログインユーザー名を更新しセッションに格納
		MemberLoginForm loginUser = (MemberLoginForm) session.getAttribute("loginUser");
		loginUser.setMemberName(form.getMemberName());
		session.setAttribute("loginUser", loginUser);

		// 会員情報完了画面へ遷移
		return "editComplete";
	}
}