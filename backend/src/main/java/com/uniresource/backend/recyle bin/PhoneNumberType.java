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
public class PhoneNumberType {
    public long phone_number_type_id;
	public String type;

	public PhoneNumberType(long phone_number_type_id_,String type_)
	{
		this.phone_number_type_id = phone_number_type_id_;
		this.type = type_;
	}
}