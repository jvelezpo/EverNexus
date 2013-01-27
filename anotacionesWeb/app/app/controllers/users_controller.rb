class UsersController < ApplicationController
  include ApplicationHelper
  def index
    @title = "My Account"
    @user = current_user
  end

  def signin
    user = User.authenticate(params[:name], params[:pass])
    if user.nil?
      flash[:error] = "Wrong email/password combination"
      redirect_to root_path
    else
      sign_in user
      redirect_to user_path
    end
  end

  def destroy
    sign_out
    redirect_to root_path
  end

  private

  def authenticate
    deny_access unless signed_in?
  end
end
