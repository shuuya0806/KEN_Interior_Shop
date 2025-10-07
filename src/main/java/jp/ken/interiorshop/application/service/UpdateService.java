package jp.ken.interiorshop.application.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ken.interiorshop.domain.entity.MemberEntity;
import jp.ken.interiorshop.domain.repository.MemberRepository;
import jp.ken.interiorshop.presentation.form.MemberRegistForm;

@Service
public class UpdateService {

	private final MemberRepository memberRepository;
	private final ModelMapper modelMapper;

	public UpdateService(MemberRepository memberRepository, ModelMapper modelMapper) {
		this.memberRepository = memberRepository;
		this.modelMapper = modelMapper;
	}

	// 電話番号の重複チェック
	public boolean isPhoneNumberDuplicated(String phoneNumber, int memberId) {
		MemberEntity existing = memberRepository.findByPhoneNumber(phoneNumber);
		return existing != null && existing.getMemberId() != memberId;
	}

	// メールアドレスの重複チェック
	public boolean isMailDuplicated(String mail, int memberId) {
		MemberEntity existing = memberRepository.findByMail(mail);
		return existing != null && existing.getMemberId() != memberId;
	}

	// 会員情報更新
	@Transactional(rollbackFor = Exception.class)
	public int updateMember(MemberRegistForm form) {
		MemberEntity entity = modelMapper.map(form, MemberEntity.class);
		return memberRepository.memberUpdate(entity);
	}
}