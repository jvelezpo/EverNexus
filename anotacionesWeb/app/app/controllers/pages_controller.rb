class PagesController < ApplicationController
  protect_from_forgery
  include ApplicationHelper

  before_filter :user_account, :only => [:index]

  def index
    @title = "Home"
  end

  def login
    user = User.authenticate(params["user"],params["pass"])
    if user.nil?
      respond = {log: false}
    else
      respond = {log: true, token: user.id}
    end
    respond_to do |format|
      format.json { render :json => respond }
    end
  end

  def note
    begin
      user = User.find(params["token"])
      #When the user is new
      if user.text.nil?
        user.text = Text.create(title: "New Note!", body: "...")
      end
      respond = {log: true, title: user.text.title, body: user.text.body}
    rescue
      respond = {log: false}
    end

    respond_to do |format|
      format.json { render :json => respond }
    end
  end

  def update_note
    begin
      user = User.find(params["token"])
      user.text.title = params["title"]
      user.text.body = params["body"]
      if user.text.save
        respond = {log: true, title: user.text.title, body: user.text.body}
      elsif
        respond = {log: false}
      end
    rescue
      respond = {log: false}
    end
    respond_to do |format|
      format.json { render :json => respond }
    end
  end


  def error
    @title = "Error"
  end

  private

  def user_account
    redirect_to user_path if signed_in?
  end
end
