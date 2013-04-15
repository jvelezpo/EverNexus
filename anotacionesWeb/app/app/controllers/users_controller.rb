class UsersController < ApplicationController
  include ApplicationHelper
  def index
    @title = "My Account"
    @user = current_user
  end

  def updatenote
    begin
      user = User.find(current_user.id)
      user.text.title = params["title"]
      user.text.body = params["body"]

      if user.text.save
        flash[:success] = "Note Saved!!!"
        redirect_to user_path
      elsif
        flash[:error] = 'Error: Data couldn\'t be save, please try again'
        redirect_to user_path
      end
    rescue
      flash[:error] = 'ERROR: Something when wrong'
      redirect_to user_path
    end

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
