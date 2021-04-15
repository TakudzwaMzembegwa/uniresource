/**package com.uniresource.backend.user.Entity;
------------------------------------------------
 * 
 * 
 * USING ONE PHONE NUMBER 
 * PUT IN USER ENTITY AS A FIELD
 * 
 * 
 * 
 * 
 * 
 */
public class PhoneNumber {
    public long phone_number_id;
	public String country_code;
	public String number;
	public long phone_number_type_id;
	public int user_id;

	public PhoneNumber(long phone_number_id_,String country_code_,String number_,long phone_number_type_id_,int user_id_)
	{
		this.phone_number_id = phone_number_id_;
		this.country_code = country_code_;
		this.number = number_;
		this.phone_number_type_id = phone_number_type_id_;
		this.user_id = user_id_;
	}
}