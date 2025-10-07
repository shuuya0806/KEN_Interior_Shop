package jp.ken.interiorshop.application.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jp.ken.interiorshop.domain.entity.MemberEntity;
import jp.ken.interiorshop.domain.repository.MemberRepository;
import jp.ken.interiorshop.presentation.form.MemberRegistForm;

@Service
public class RegistService {

	private final MemberRepository memberRepository;
	private final ModelMapper modelMapper;

	public RegistService(MemberRepository memberRepository, ModelMapper modelMapper) {
		this.memberRepository = memberRepository;
		this.modelMapper = modelMapper;
	}
	
	// メールアドレス重複チェック専用（自分自身を除外）
	public boolean isMailDuplicated(String mail) {
	    return memberRepository.existsByMail(mail);
	}

	// 電話番号の重複チェック専用（自分自身を除外）
	public boolean isPhoneNumberDuplicated(String phoneNumber) {
	    return memberRepository.existsByPhoneNumber(phoneNumber);
	}
	
	//会員登録
	 public int registMembers(MemberRegistForm form) {
		MemberEntity entity = modelMapper.map(form, MemberEntity.class);
		return memberRepository.regist(entity);
	}
}