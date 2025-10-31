package uz.pdp.shopapplication.service;

import uz.pdp.shopapplication.dto.UserDto;

public interface DocxService {

    byte[] generateUserCertificate(UserDto user);

}
