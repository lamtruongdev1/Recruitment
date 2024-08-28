package com.poly.Recruitment.service.serviceImplement;

import com.poly.Recruitment.entity.NhaTuyenDung;
import com.poly.Recruitment.repository.NhaTuyenDungDAO;
import com.poly.Recruitment.service.NhaTuyenDungService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NhaTuyenDungServiceImpl implements NhaTuyenDungService {
    private final NhaTuyenDungDAO nhaTuyenDungDAO;
    @Override
    public NhaTuyenDung save(NhaTuyenDung nhaTuyenDung) {
        return nhaTuyenDungDAO.save(nhaTuyenDung);
    }
    @Override
    public Optional<NhaTuyenDung> findById(Long id) {
        return nhaTuyenDungDAO.findById(id);
    }
}
