class PagesController < ApplicationController
  def index

  end

  def nota
    send_file "#{Rails.root}/app/assets/raw/prueba.txt", :type => "text; charset=utf-8", :disposition => 'inline'
  end

  def questions
    send_file "#{Rails.root}/app/assets/raw/questions.xml", :type => "application/xml", :disposition => 'inline'
  end
end
