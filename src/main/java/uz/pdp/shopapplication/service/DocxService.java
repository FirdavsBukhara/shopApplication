package uz.pdp.shopapplication.service;

import uz.pdp.shopapplication.dto.UserDto;

public interface DocxService {

    byte[] generateUserCertificatePdf(UserDto user);

}
