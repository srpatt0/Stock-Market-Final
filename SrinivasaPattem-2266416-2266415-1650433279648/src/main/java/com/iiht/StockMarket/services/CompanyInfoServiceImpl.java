package com.iiht.StockMarket.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;

import com.iiht.StockMarket.dto.CompanyDetailsDTO;
import com.iiht.StockMarket.model.CompanyDetails;
import com.iiht.StockMarket.repository.CompanyInfoRepository;
import com.iiht.StockMarket.utils.StockMarketUtility;

@Service
@Transactional
public class CompanyInfoServiceImpl implements CompanyInfoService {
	
	@Autowired
	private CompanyInfoRepository repository; 
	
	public CompanyDetailsDTO saveCompanyDetails(CompanyDetailsDTO companyDetailsDTO) { 
        CompanyDetails companyDetails=StockMarketUtility.convertToCompanyDetails(companyDetailsDTO); 
		return StockMarketUtility.convertToCompanyDetailsDTO(repository.save(companyDetails));
	};
	//----------------------------------------------------------------------------
	public CompanyDetailsDTO deleteCompany(Long companyCode) {
        CompanyDetails companyDetails=repository.findById(companyCode).orElse(null);
        if(companyDetails!=null )
        {
            repository.deleteById(companyCode);
            return StockMarketUtility.convertToCompanyDetailsDTO(companyDetails);
        }

		return null;
	};
	//----------------------------------------------------------------------------
	public CompanyDetailsDTO getCompanyInfoById(Long companyCode) {

        CompanyDetails companyDetails=repository.findById(companyCode).orElse(null);
        if(companyDetails!=null)
            return StockMarketUtility.convertToCompanyDetailsDTO(companyDetails);
		return null;
	};
	
	//----------------------------------------------------------------------------
	public List<CompanyDetailsDTO> getAllCompanies() {
       List<CompanyDetails> companyList= repository.findAll();
       if(companyList!=null)
       {
           List<CompanyDetailsDTO> companyListDTO = new ArrayList<CompanyDetailsDTO>();
           for(CompanyDetails company : companyList)
           {
               companyListDTO.add(StockMarketUtility.convertToCompanyDetailsDTO(company));
           }
            return companyListDTO;
       }
		return null;
	}
}