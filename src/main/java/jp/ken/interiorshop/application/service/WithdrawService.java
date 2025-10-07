package jp.ken.interiorshop.application.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttributes;

import jp.ken.interiorshop.domain.entity.MemberEntity;
import jp.ken.interiorshop.domain.repository.MemberRepository;
import jp.ken.interiorshop.presentation.form.MemberLoginForm;

@Service
@SessionAttributes("loginUser")
public class WithdrawService {

	private final MemberRepository memberRepository;
	private final ModelMapper modelMapper;

	public WithdrawService(MemberRepository memberRepository, ModelMapper modelMapper) {
		this.memberRepository = memberRepository;
		this.modelMapper = modelMapper;
	}

	//会員退会処理
	@Transactional(rollbackFor = Exception.class)
	public int withdrawMember(MemberLoginForm form) throws Exception {
		MemberEntity entity = modelMapper.map(form, MemberEntity.class);
		return memberRepository.memberWithdraw(form, entity);
	}
}