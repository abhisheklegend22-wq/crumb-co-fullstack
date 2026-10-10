import { CommonModule } from '@angular/common';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { environment } from '../environments/environment';

type Product = { id:number; name:string; category:string; price:number; description:string; emoji:string; stock:number; active:boolean };
type CartLine = { product:Product; quantity:number };
type OrderResponse = { id:number; total:number; status:string; createdAt:string };
@Component({ selector:'app-root', standalone:true, imports:[CommonModule,FormsModule], templateUrl:'./app.component.html' })
export class AppComponent implements OnInit {
  private api = environment.apiUrl;
  products:Product[]=[]; cart:CartLine[]=[]; category='All'; search=''; loading=true; busy=false;
  showCheckout=false; showAdmin=false; toastMessage=''; errorMessage=''; successOrder:OrderResponse|null=null;
  customer={customerName:'',email:'',phone:'',address:'',city:''}; newsletterEmail=''; newsletterMessage=''; adminOrders:any[]=[]; adminProducts:Product[]=[];
  newProduct={name:'',category:'Pastries',price:0,description:'',emoji:'🥐',stock:10};
  categories=['All','Pastries','Cakes','Cookies','Bread'];
  constructor(private http:HttpClient){}
  ngOnInit(){this.loadProducts();}
  loadProducts(){this.loading=true;this.http.get<Product[]>(`${this.api}/products`).subscribe({next:p=>{this.products=p;this.loading=false;},error:()=>{this.loading=false;this.errorMessage='Cannot connect to the backend. Start MySQL and the Spring Boot API, then refresh.';}});}
  get visibleProducts(){const q=this.search.trim().toLowerCase();return this.products.filter(p=>(this.category==='All'||p.category===this.category)&&(`${p.name} ${p.description} ${p.category}`).toLowerCase().includes(q));}
  get count(){return this.cart.reduce((n,l)=>n+l.quantity,0)}
  get subtotal(){return this.cart.reduce((n,l)=>n+l.product.price*l.quantity,0)}
  get delivery(){return this.subtotal===0||this.subtotal>=799?0:49}
  get total(){return this.subtotal+this.delivery}
  add(p:Product){if(p.stock<1){this.toast('Sorry, this item is out of stock.');return;}const line=this.cart.find(l=>l.product.id===p.id);if(line){if(line.quantity>=p.stock){this.toast('You have reached available stock.');return;}line.quantity++;}else this.cart.push({product:p,quantity:1});this.toast(`${p.name} added to your bag!`)}
  change(line:CartLine,delta:number){line.quantity+=delta;if(line.quantity<=0)this.cart=this.cart.filter(x=>x!==line);}
  openCheckout(){if(!this.cart.length){this.toast('Add something delicious first!');return;}this.successOrder=null;this.showCheckout=true;}
  placeOrder(){if(this.busy||!this.cart.length)return;this.busy=true;this.errorMessage='';const payload={...this.customer,items:this.cart.map(l=>({productId:l.product.id,quantity:l.quantity}))};
    this.http.post<OrderResponse>(`${this.api}/orders`,payload).subscribe({next:order=>{this.successOrder=order;this.cart=[];this.busy=false;this.loadProducts();},error:(e:HttpErrorResponse)=>{this.busy=false;this.errorMessage=e.error?.detail||e.error?.message||'Could not place order. Check stock and backend connection.';}});}
  closeCheckout(){this.showCheckout=false;this.successOrder=null;this.errorMessage='';}
  subscribe(){if(!this.newsletterEmail.trim())return;this.http.post(`${this.api}/newsletter`,{email:this.newsletterEmail}).subscribe({next:()=>{this.newsletterMessage='You’re on the list! Welcome to the sweet side.';this.newsletterEmail='';},error:(e:HttpErrorResponse)=>this.newsletterMessage=e.status===409?'This email is already subscribed.':'Could not subscribe. Please check the backend.'});}
  toggleAdmin(){this.showAdmin=!this.showAdmin;if(this.showAdmin){this.http.get<Product[]>(`${this.api}/products`).subscribe(p=>this.adminProducts=p);this.http.get<any[]>(`${this.api}/orders`).subscribe({next:o=>this.adminOrders=o,error:()=>this.adminOrders=[]});}}
  createProduct(){this.http.post<Product>(`${this.api}/products`,this.newProduct).subscribe({next:p=>{this.toast('Product saved to MySQL.');this.products.push(p);this.newProduct={name:'',category:'Pastries',price:0,description:'',emoji:'🥐',stock:10};},error:()=>this.toast('Could not save product. Check the fields and API.')});}
  deleteProduct(p:Product){this.http.delete(`${this.api}/products/${p.id}`).subscribe({next:()=>{this.products=this.products.filter(x=>x.id!==p.id);this.adminProducts=this.adminProducts.filter(x=>x.id!==p.id);this.toast('Product deactivated.');},error:()=>this.toast('Could not update product.')});}
  money(value:number){return new Intl.NumberFormat('en-IN',{style:'currency',currency:'INR',maximumFractionDigits:2}).format(value)}
  toast(message:string){this.toastMessage=message;setTimeout(()=>this.toastMessage='',2500)}
}
