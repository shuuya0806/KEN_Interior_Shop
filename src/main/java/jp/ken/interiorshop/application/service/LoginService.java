package jp.ken.interiorshop.application.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jp.ken.interiorshop.domain.entity.MemberEntity;
import jp.ken.interiorshop.domain.repository.MemberRepository;
import jp.ken.interiorshop.presentation.form.MemberLoginForm;
import jp.ken.interiorshop.presentation.form.MemberRegistForm;

@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public LoginService(MemberRepository memberRepository, ModelMapper modelMapper) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }

    /*
     * ログイン処理（メール・パスワードで認証）
     */
    public MemberLoginForm doLogin(MemberLoginForm form) throws Exception {
        String mail = form.getMail();
        String password = form.getPassword();

        MemberEntity entity = memberRepository.getLoginData(mail, password);
        if (entity == null) {
        	// 認証失敗時は null を返す
        	return null; 
        }

        return modelMapper.map(entity, MemberLoginForm.class);
    }

    // 会員IDを元に会員情報取得
    public MemberRegistForm getLoginData(int memberId) throws Exception {
        MemberEntity entity = memberRepository.findByMemberId(memberId);
        if (entity == null) {
            return null;
        }

        return modelMapper.map(entity, MemberRegistForm.class);
    }
}