module ApplicationHelper
  def title
    base_title = "EverNexus"
    if @title.nil?
      base_title
    else
      "#{base_title} | #{@title}"
    end
  end

  def sign_in(user)
    cookies.permanent.signed[:user] = [user.name, user.pass]
    self.current_user = user
  end

  def sign_out
    cookies.delete(:user)
    self.current_user = nil
  end

  def current_user=(user)
    @current_user = user
  end

  def current_user
    @current_user ||= user_remember
  end

  def signed_in?
    !current_user.nil?
  end

  def deny_access
    flash[:notice] = "Please signIn!!"
    redirect_to root_path
  end

  def days_between(date1, date2)
    days = (date1 - date2).to_i
    days/1.days
  end

  private

  def user_remember
    User.authenticate(*remember_token)
  end

  def remember_token
    cookies.signed[:user] || [nil, nil]
  end
end
