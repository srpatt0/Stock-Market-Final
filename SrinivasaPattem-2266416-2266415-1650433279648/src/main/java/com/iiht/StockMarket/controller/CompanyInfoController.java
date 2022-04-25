package com.iiht.StockMarket.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iiht.StockMarket.dto.CompanyDetailsDTO;
import com.iiht.StockMarket.dto.InvalidCompanyExceptionResponse;
import com.iiht.StockMarket.exception.CompanyNotFoundException;
import com.iiht.StockMarket.exception.InvalidCompanyException;
import com.iiht.StockMarket.services.CompanyInfoService;

@RestController
@RequestMapping (value = "/company")
public class CompanyInfoController {

	@Autowired
	private CompanyInfoService companyInfoService;

	//-------------------------------------------------------------------------------------------------------------------------------
	// SERVICE OPERATIONS
	//-------------------------------------------------------------------------------------------------------------------------------
	
	@PostMapping(value="/addCompany")																					// 3. WORKING
	public ResponseEntity<CompanyDetailsDTO> addCompanyDetails(@Valid @RequestBody CompanyDetailsDTO companyDetailsDTO, BindingResult bindingResult) throws InvalidCompanyException {
        
        String message="";
        boolean flag=true;

        //DTO Field validation Constraints
        
        if(bindingResult.hasErrors())
        {
            for(FieldError err : bindingResult.getFieldErrors())
            {
                if(flag)
                {
                    message=err.getDefaultMessage();
                    flag=false;
                }
                else
                {
                    message=message+ " || "+err.getDefaultMessage();
                }


            }
            throw new InvalidCompanyException(message);
        }
        // Uniqueness check Constraint
        if(companyInfoService.getCompanyInfoById(companyDetailsDTO.getCompanyCode())!=null)
        {
            throw new InvalidCompanyException("Custom error: Company Code "+companyDetailsDTO.getCompanyCode()+" already exists");
        }


        
        return new ResponseEntity<CompanyDetailsDTO>(companyInfoService.saveCompanyDetails(companyDetailsDTO),HttpStatus.OK);
	}
	//-------------------------------------------------------------------------------------------------------------------------------
	@DeleteMapping(value = "/deleteCompany/{companyCode}")																// 4. WORKING
	public ResponseEntity<CompanyDetailsDTO> deleteCompanyDetails(@PathVariable("companyCode") Long companyCode) {
        
        CompanyDetailsDTO companyDetailsDTO = companyInfoService.deleteCompany(companyCode);
        if(companyDetailsDTO ==null)

         throw new CompanyNotFoundException("Custom Error: Company with Company Code"+companyCode+" Doesn't exist");

         else
         
         return new ResponseEntity<CompanyDetailsDTO>(companyDetailsDTO,HttpStatus.OK);
        
       // if(companyInfoService.getCompanyInfoById(companyCode)==null)
      //  {
      //      throw new CompanyNotFoundException("Custom Error: Company with Company Code"+companyCode+" Doesn't exist");
     //   }
	//	return new ResponseEntity<CompanyDetailsDTO>(companyInfoService.deleteCompany(companyCode),HttpStatus.OK);
	}
	//-------------------------------------------------------------------------------------------------------------------------------
	@GetMapping(value = "/getCompanyInfoById/{companyCode}")															// 5. WORKING
	public ResponseEntity<CompanyDetailsDTO> getCompanyDetailsById(@PathVariable("companyCode") Long companyCode) {
		return new ResponseEntity<CompanyDetailsDTO>(companyInfoService.getCompanyInfoById(companyCode),HttpStatus.OK);
	}
	//-------------------------------------------------------------------------------------------------------------------------------
	@GetMapping(value = "/getAllCompanies", produces = "application/json")												// 6. WORKING
	public ResponseEntity<List<CompanyDetailsDTO>> getAllCompanies() {		
		return new ResponseEntity<List<CompanyDetailsDTO>>(companyInfoService.getAllCompanies(),HttpStatus.OK);
	}
	
	//================================================================================================
	//			UTITLITY EXCEPTION HANDLERS - 2
	//================================================================================================
	@ExceptionHandler(InvalidCompanyException.class)
	public ResponseEntity<InvalidCompanyExceptionResponse> companyHandler(InvalidCompanyException ex) {
		InvalidCompanyExceptionResponse resp = new InvalidCompanyExceptionResponse(ex.getMessage(),System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value());
		ResponseEntity<InvalidCompanyExceptionResponse> response =	new ResponseEntity<InvalidCompanyExceptionResponse>(resp, HttpStatus.BAD_REQUEST);
		return response;
	}
	//------------------------------------------------------------------------------------------------
	@ExceptionHandler(CompanyNotFoundException.class)
	public ResponseEntity<InvalidCompanyExceptionResponse> companyHandler(CompanyNotFoundException ex){
		InvalidCompanyExceptionResponse resp = new InvalidCompanyExceptionResponse(ex.getMessage(),System.currentTimeMillis(), HttpStatus.NOT_FOUND.value());
		ResponseEntity<InvalidCompanyExceptionResponse> response = new ResponseEntity<InvalidCompanyExceptionResponse>(resp, HttpStatus.NOT_FOUND);
		return response;
	}	
}